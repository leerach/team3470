 waitForStart();
        if (opModeIsActive()) {
            telemetry.addData("IsFound", detector.isFound()); // Is the gold mineral on the screen
            telemetry.addData("X Pos", detector.getXPosition()); // Gold X pos.

            //unlatch section
            rack.setPower(-1);
            sleep(1400);
            rack.setPower(0);
            sleep(200);
            /*
            frontLeft.setPower(0.2);
            left.setPower(-0.2);
            frontRight.setPower(0.2);
            right.setPower(-0.2);
            sleep(400);
            frontLeft.setPower(0);
            left.setPower(0);
            frontRight.setPower(0);
            right.setPower(0);
            */

            // get into position to start sampling
            moveLeft(0.2, 400);
            sleep(200);
            moveForward(.2, 500);
            sleep(200);
            moveRight(.2, 200);
            sleep(200);
            moveForward(.2, 500);
            sleep(200);
            // time taken to move gold block
            int pushTime =800;
            // sleep times between motor movements
            int sleepTime = 250;
            //Check center for cube
            if(detector.isFound()) {
                moveForward(.2, pushTime);
                sleep(sleepTime);
                moveBackward(.2, pushTime);
                sleep(sleepTime);
                moveLeft(.4, 1300);
            } else {
                // get into position to check right cube
                moveRight(.2, 1800);
                sleep(500);
                if (detector.isFound()) {
                    moveForward(.2, pushTime);
                    sleep(sleepTime);
                    moveBackward(.2, pushTime);
                    sleep(sleepTime);
                    moveLeft(.4, 2150);
                } else {
                    // assume its the left cube
                    moveLeft(.4, 1600);
                    sleep(sleepTime);
                    moveForward(.2, pushTime);
                    sleep(sleepTime);
                    moveBackward(.2, pushTime);
                    sleep(sleepTime);
                    moveLeft(.2,1200);
                }
            }

            // depot section
            sleep(sleepTime);
            moveLeft(0.2,600);
            sleep(sleepTime);
            moveForward(0.2,300);
            sleep(sleepTime);
            rotateLeft(0.4,1120);
            sleep(sleepTime);
            moveRight(0.4,1100);
            sleep(sleepTime);
            moveLeft(0.2,200);
            sleep(sleepTime);
            moveBackward(0.6,1000);

            //Drop down flag
            sleep(200);
            flag.setPower(-0.75);
            sleep(250);
            flag.setPower(0);


            //Go away to end
            sleep(400);
            frontLeft.setPower(-1);
            left.setPower(-1);
            frontRight.setPower(0.95);
            right.setPower(0.95);
            sleep(1900);
            frontLeft.setPower(0);
            left.setPower(0);
            frontRight.setPower(0);
            right.setPower(0);





            telemetry.update();
            detector.disable();
        }
    }

    public void moveLeft(double speed, int time) {
        frontLeft.setPower(speed);
        left.setPower(-speed);
        frontRight.setPower(speed);
        right.setPower(-speed);
        sleep(time);
        frontLeft.setPower(0);
        left.setPower(0);
        frontRight.setPower(0);
        right.setPower(0);
    }
    public void moveRight(double speed, int time) {
        frontLeft.setPower(-speed);
        left.setPower(speed);
        frontRight.setPower(-speed);
        right.setPower(speed);
        sleep(time);
        frontLeft.setPower(0);
        left.setPower(0);
        frontRight.setPower(0);
        right.setPower(0);
    }
    public void moveBackward(double speed, int time) {
        frontLeft.setPower(speed);
        left.setPower(speed);
        frontRight.setPower(-speed);
        right.setPower(-speed);
        sleep(time);
        frontLeft.setPower(0);
        left.setPower(0);
        frontRight.setPower(0);
        right.setPower(0);
    }
    public void moveForward(double speed, int time) {
        frontLeft.setPower(-speed);
        left.setPower(-speed);
        frontRight.setPower(speed);
        right.setPower(speed);
        sleep(time);
        frontLeft.setPower(0);
        left.setPower(0);
        frontRight.setPower(0);
        right.setPower(0);
    }

    public void rotateLeft(double speed, int time) {
        frontLeft.setPower(speed);
        left.setPower(speed);
        frontRight.setPower(speed);
        right.setPower(speed);
        sleep(time);
        frontLeft.setPower(0);
        left.setPower(0);
        frontRight.setPower(0);
        right.setPower(0);
    }

    public void rotateRight(double speed, int time) {
        frontLeft.setPower(-speed);
        left.setPower(-speed);
        frontRight.setPower(-speed);
        right.setPower(-speed);
        sleep(time);
        frontLeft.setPower(0);
        left.setPower(0);
        frontRight.setPower(0);
        right.setPower(0);
    }

}
