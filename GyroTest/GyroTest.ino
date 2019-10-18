static int Lfwd = 1900;
static int Rfwd = 1100;
static int Lbck = 1100;
static int Rbck = 1900;
static int stp = 1500;

static int manLturn = 1800;
static int manRturn = 1200;

static int turnDeg;
static int turnSpeed;   //1600 for left turn, 1400 for right turn

char command = '0';
double z_deg;
int BTstate;
int proximity = 0;
int currentDistance = 0;
int oldDistance = 0;
int clothChange = 0;
String nextTurn = "left";
int threshold = 100;

void setup()
{
  InitEmoro();                                                    // Initializes all available inputs and outputs on EMoRo 2560.
  EmoroServo.attach(SERVO_0);                                     // Initiates servo on SERVO_1 port.
  EmoroServo.attach(SERVO_1);                                     // Initiates servo on SERVO_2 port.
  Gyr.init();
  Ultrasonic.attach(GPP_0);
}
void loop()
{
  Lcd.locate(0, 0);
  Lcd.print(oldDistance);
  BTstate = Bluetooth.connection();                     // initialize variable - store last Bluetooth state
  Lcd.locate(1, 0);                                               // set LCD cursor position (row, column)
  if (BTstate == 0)                                         // if connection is established
    Lcd.print("Not connected");                                   // print constant string
  else
    Lcd.print("Connected");

  /*
     Commands:
     M = manual mode
     A = auto mode
     C = circle shape
     S = square shape
     T = triangle shape
     1 = GO
     0 = STOP
     F = drive forward
     B = drive back
     L = turn left
     R = turn right
     Q = quit to main menu
  */
  if (Serial1.available())
  {
    command = (char)Serial1.read();   //receives the issued command from the app

    boolean GO = false;

    if (command == 'M') //initiate manual mode
    {
      while (command != 'A')
      {
        Lcd.locate(0, 0);
        Lcd.print(oldDistance);
        command = (char)Serial1.read();

        switch (command)
        {
          case 'P':
            EmoroServo.write(SERVO_0, stp);
            EmoroServo.write(SERVO_1, stp);
            if (currentDistance != oldDistance)
            {
              Serial1.print(currentDistance - oldDistance);
              oldDistance = currentDistance;
              if (oldDistance >= 10)
              {
                while (command != 'C')
                {
                  command = (char)Serial1.read();
                }
                oldDistance = 0;
                currentDistance = 0;
              }
            }
            break;
          case 'A': //ends manual mode
            break;
          case 'F': //forward
            EmoroServo.write(SERVO_0, Lfwd);
            EmoroServo.write(SERVO_1, Rfwd);
            currentDistance++;
            break;
          case 'B': //back
            EmoroServo.write(SERVO_0, Lbck);
            EmoroServo.write(SERVO_1, Rbck);
            currentDistance++;
            break;
          case 'L': //left
            EmoroServo.write(SERVO_0, manLturn);
            EmoroServo.write(SERVO_1, manLturn);
            break;
          case 'R': //right
            EmoroServo.write(SERVO_0, manRturn);
            EmoroServo.write(SERVO_1, manRturn);
            break;
        }
      }
    }
    else if (command == 'A') //initiate auto mode
    {
      while (command != 'M')
      {
        proximity = 0;
        command = (char)Serial1.read();
        if (command == '1')
        {
          GO = true;
          while (GO)
          {
            if (nextTurn == "left")
            {
              turnSpeed = 1600;
              turnDeg = 280;
            }
            else if (nextTurn == "right")
            {
              turnSpeed = 1400;
              turnDeg = 80;
            }
            command = (char)Serial1.read();
            if (command == '0' || command == 'M')
            {
              GO = false;
              EmoroServo.write(SERVO_0, stp);
              EmoroServo.write(SERVO_1, stp);
              break;
            }
            Gyr.setDegrees(0, 0, 0);
            z_deg = Gyr.readDegreesZ();

            // go forward:
            EmoroServo.write(SERVO_0, Lfwd);                              // go forward with left servo
            EmoroServo.write(SERVO_1, Rfwd);                              // go forward with right servo

            proximity = Ultrasonic.read(GPP_0); //Checks if the robot has reached the edge of the table
            if (proximity > threshold)
            {
              EmoroServo.write(SERVO_0, stp);
              EmoroServo.write(SERVO_1, stp);

              // rotate until direction is reached (with +-2 degrees tolerance)
              while (z_deg < (turnDeg - 2) || z_deg > (turnDeg + 2))
              {
                command = (char)Serial1.read();
                if (command == '0' || command == 'M')
                {
                  GO = false;
                  EmoroServo.write(SERVO_0, stp);
                  EmoroServo.write(SERVO_1, stp);
                  break;
                }
                z_deg = Gyr.readDegreesZ();

                EmoroServo.write(SERVO_0, turnSpeed);                              // go backwards with left servo
                EmoroServo.write(SERVO_1, turnSpeed);                              // go forward with right servo
              }
              proximity = Ultrasonic.read(GPP_0);
              if (proximity > threshold)
              {
                GO = false;
                EmoroServo.write(SERVO_0, stp);
                EmoroServo.write(SERVO_1, stp);
                break;
              }
              else
              {
                EmoroServo.write(SERVO_0, Lfwd);
                EmoroServo.write(SERVO_1, Rfwd);
                delay(100);
                Gyr.setDegrees(0, 0, 0);
                z_deg = Gyr.readDegreesZ();
                while (z_deg < (turnDeg - 2) || z_deg > (turnDeg + 2))
                {
                  command = (char)Serial1.read();
                  if (command == '0' || command == 'M')
                  {
                    GO = false;
                    EmoroServo.write(SERVO_0, stp);
                    EmoroServo.write(SERVO_1, stp);
                    break;
                  }
                  z_deg = Gyr.readDegreesZ();

                  EmoroServo.write(SERVO_0, turnSpeed);                              // go backwards with left servo
                  EmoroServo.write(SERVO_1, turnSpeed);                              // go forward with right servo
                }
              }
              if (nextTurn == "left")
              {
                nextTurn = "right";
              }
              else if (nextTurn == "right")
              {
                nextTurn = "left";
              }
            }
          }
        }
        EmoroServo.write(SERVO_0, stp);
        EmoroServo.write(SERVO_1, stp);
      }
    }
  }
}
