import sys
from PyQt5.QtWidgets import QApplication, QWidget, QPushButton, QLabel, QVBoxLayout, QHBoxLayout, QGraphicsView, QGraphicsScene, QGraphicsRectItem, QFrame
from PyQt5.QtCore import QTimer, Qt
from PyQt5.QtGui import QFont, QColor, QBrush, QPainter
import requests

# URL des services
presence_sensor_url = "http://localhost:8081/api/presence-sensor/status"
light_sensor_url = "http://localhost:8082/api/light-sensor/value"
light_actuator_url = "http://localhost:8083/api/light-actuator/command/"

class LightControlApp(QWidget):
    def __init__(self):
        super().__init__()
        self.light_on = False
        self.auto_mode = False  # Indicateur de mode automatique ou manuel
        self.last_presence = None  # Dernier état de présence pour éviter les changements rapides
        self.last_light_intensity = None  # Dernière intensité lumineuse pour éviter les changements rapides
        self.init_ui()

    def init_ui(self):
        self.setWindowTitle('Light Monitoring')

        # Définir la taille de la fenêtre
        self.setGeometry(400, 200, 600, 400)

        # Appliquer un style sombre global
        self.setStyleSheet("""
            QWidget {
                background-color: #2E2E2E;
                color: white;
                font-family: 'Arial';
                font-size: 14px;
            }
            QLabel {
                color: #B8B8B8;
            }
            QPushButton {
                background-color: #444444;
                color: white;
                border: 2px solid #666666;
                border-radius: 5px;
                padding: 10px;
            }
            QPushButton:hover {
                background-color: #555555;
            }
            QPushButton:pressed {
                background-color: #333333;
            }
        """)

        # Créer les composants de l'interface
        self.status_label = QLabel('Light Status: OFF', self)
        self.status_label.setStyleSheet("font-size: 18px; padding: 5px;")
        self.status_label.setFrameStyle(QFrame.Panel | QFrame.Sunken)
        self.status_label.setLineWidth(2)

        self.toggle_button = QPushButton('Turn ON', self)
        self.toggle_button.setStyleSheet("background-color: #4CAF50; font-size: 16px; padding: 10px;")
        self.toggle_button.setFixedWidth(150)

        self.presence_label = QLabel('Presence Detected: No', self)
        self.presence_label.setStyleSheet("font-size: 14px; padding: 5px;")
        self.presence_label.setFrameStyle(QFrame.Panel | QFrame.Sunken)
        self.presence_label.setLineWidth(2)

        self.light_intensity_label = QLabel('Light Intensity: 0 lux', self)
        self.light_intensity_label.setStyleSheet("font-size: 14px; padding: 5px;")
        self.light_intensity_label.setFrameStyle(QFrame.Panel | QFrame.Sunken)
        self.light_intensity_label.setLineWidth(2)

        # Créer un layout vertical pour les informations
        info_layout = QVBoxLayout()
        info_layout.addWidget(self.status_label)
        info_layout.addWidget(self.toggle_button)
        info_layout.addWidget(self.presence_label)
        info_layout.addWidget(self.light_intensity_label)

        # Créer un bouton pour basculer entre mode manuel et automatique
        self.mode_toggle_button = QPushButton('Switch to Auto Mode', self)
        self.mode_toggle_button.setStyleSheet("background-color: #007bff; color: white; font-size: 16px; padding: 10px; border-radius: 5px;")
        self.mode_toggle_button.setFixedWidth(180)
        self.mode_toggle_button.clicked.connect(self.toggle_mode)

        # Créer un layout horizontal pour la pièce et les informations
        main_layout = QHBoxLayout()

        # Créer la scène graphique (représentation de la pièce)
        self.scene = QGraphicsScene(self)
        self.view = QGraphicsView(self.scene, self)
        self.view.setGeometry(50, 50, 250, 250)

        # Activer l'anticrénelage et le rendu optimisé
        self.view.setRenderHint(QPainter.Antialiasing)
        self.view.setRenderHint(QPainter.SmoothPixmapTransform)

        # Créer un rectangle représentant la pièce
        self.room_rect = QGraphicsRectItem(0, 0, 250, 250)
        self.room_rect.setBrush(QBrush(QColor(255, 255, 255)))  # Fond blanc pour commencer (lumière éteinte)
        self.scene.addItem(self.room_rect)

        # Ajouter les composants au layout principal
        main_layout.addWidget(self.view)
        main_layout.addLayout(info_layout)
        main_layout.addWidget(self.mode_toggle_button)

        # Centrer les éléments dans la fenêtre
        self.setLayout(main_layout)

        # Connecter le bouton à l'action
        self.toggle_button.clicked.connect(self.toggle_light)

        # Mettre à jour les informations périodiquement
        self.update_info()

    def toggle_light(self):
        # Changer l'état de la lumière (ON/OFF)
        if not self.auto_mode:  # Ne pas envoyer de commande en mode automatique
            command = "ON" if not self.light_on else "OFF"
            self.send_light_command(command)
            self.light_on = not self.light_on

        # Mettre à jour l'interface
        self.status_label.setText(f"Light Status: {'ON' if self.light_on else 'OFF'}")
        self.toggle_button.setText(f"Turn {'OFF' if self.light_on else 'ON'}")
        self.status_label.setStyleSheet(f"color: {'#4CAF50' if self.light_on else '#FF6347'}; font-size: 18px; padding: 5px;")
        self.toggle_button.setStyleSheet(f"background-color: {'#FF6347' if self.light_on else '#4CAF50'}; font-size: 16px; padding: 10px;")

        # Mettre à jour le fond de la pièce (lumière)
        if self.light_on:
            self.room_rect.setBrush(QBrush(QColor(255, 255, 153)))  # Jaune clair pour simuler la lumière allumée
        else:
            self.room_rect.setBrush(QBrush(QColor(255, 255, 255)))  # Blanc pour simuler la lumière éteinte

    def toggle_mode(self):
        # Bascule entre le mode manuel et automatique
        self.auto_mode = not self.auto_mode
        if self.auto_mode:
            self.mode_toggle_button.setText('Switch to Manual Mode')
            self.status_label.setText('Light Status: AUTO (No Command Sent)')
        else:
            self.mode_toggle_button.setText('Switch to Auto Mode')
            self.status_label.setText(f"Light Status: {'ON' if self.light_on else 'OFF'}")

    def send_light_command(self, command):
        url = light_actuator_url + command
        try:
            response = requests.post(url, json={"command": command})
            if response.status_code == 200:
                print(f"Command {command} sent successfully.")
            else:
                print(f"Failed to send command {command}.")
        except Exception as e:
            print(f"Error sending command: {e}")

    def update_info(self):
        try:
            # Récupérer les informations de présence et de luminosité
            presence_response = requests.get(presence_sensor_url)
            light_response = requests.get(light_sensor_url)

            if presence_response.status_code == 200:
                presence_detected = presence_response.json()
                self.presence_label.setText(f"Presence Detected: {'Yes' if presence_detected else 'No'}")
            if light_response.status_code == 200:
                light_intensity = light_response.json()
                self.light_intensity_label.setText(f"Light Intensity: {light_intensity} lux")

            # Si le mode est automatique, on met à jour l'état de la lumière sans envoyer de commande
            if self.auto_mode:
                presence_detected = presence_response.json()
                light_intensity = light_response.json()

                # Eviter les changements rapides de lumière
                if presence_detected != self.last_presence or light_intensity != self.last_light_intensity:
                    # Si la présence est détectée et l'intensité lumineuse est faible, on allume la lumière
                    if presence_detected and light_intensity < 300 and not self.light_on:
                        self.light_on = True
                        self.room_rect.setBrush(QBrush(QColor(255, 255, 153)))  # Jaune clair pour simuler la lumière allumée
                    # Si la présence n'est pas détectée ou si l'intensité est suffisante, on éteint la lumière
                    elif not presence_detected or light_intensity >= 300 and self.light_on:
                        self.light_on = False
                        self.room_rect.setBrush(QBrush(QColor(255, 255, 255)))  # Blanc pour simuler la lumière éteinte

                    # Mettre à jour les derniers états détectés pour éviter les changements rapides
                    self.last_presence = presence_detected
                    self.last_light_intensity = light_intensity

        except requests.exceptions.RequestException as e:
            print(f"Error fetching data: {e}")

        # Actualiser les informations toutes les 2 secondes
        QTimer.singleShot(2000, self.update_info)

if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = LightControlApp()
    ex.show()
    sys.exit(app.exec_())
