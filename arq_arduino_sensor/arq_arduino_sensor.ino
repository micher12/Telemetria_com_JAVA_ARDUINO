#define sensorPin 7
bool ativado = false;
void setup() {
  // put your setup code here, to run once:
  pinMode(sensorPin, INPUT);
  Serial.begin(9600);
  delay(2000);
}

void loop() {
  // put your main code here, to run repeatedly:
  bool sensor = digitalRead(sensorPin);
  if(sensor != ativado){
    if(ativado == true){
      Serial.println("Ativado");
    }else{
      Serial.println("Desativado");
    }

    ativado = sensor;
  }

}
