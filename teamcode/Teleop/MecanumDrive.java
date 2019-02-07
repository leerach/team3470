package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Use this code for the competition")


public class MecanumDrive extends LinearOpMode {

    private DcMotor rightMotor, leftMotor, frontRightMotor, frontLeftMotor, rack, arm;
    //  private DcMotor intake, extension;

    // controller state values
    double[] values = {
            0,  //0 Left Stick x
            0,  //1 Right Stick x
            0,  //2 Left Trigger
            0,  //3 Right Trigger
            0,  //4 Left Bumper
            0,  //5 Right Bumper
            0,  //6 A
            0,  //7 B
            0,  //8 X
            0,  //9 Y
            0,  //10 right stick button
            0,  //11 left stick button
            0,  //12 dpad left
            0,  //13 dpad right
            0,  //14 dpad up
            0,  //15 dpad down
            0,  //16 Left Stick y
            0,  //17 Right Stick y
            0,  //18 Start
            0,  //19 Back
    };


    public void runOpMode() throws InterruptedException {
        initialize();

        waitForStart();
        while (opModeIsActive()) {
            loopCode();
        }
    }


    public void initialize() {

        // name drive motors
        rightMotor = hardwareMap.dcMotor.get("right");  // rightMotor named (right)
        rightMotor.setDirection(DcMotor.Direction.REVERSE);  // reverse direction of right motor
        leftMotor = hardwareMap.dcMotor.get("left");   // leftMotor named (left)
        rightMotor.setPower(0);
        leftMotor.setPower(0);

        // name REV Expansion Hub 1 motors

        rack = hardwareMap.dcMotor.get("rack");
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
        frontLeftMotor.setPower(0);
        frontRightMotor = hardwareMap.dcMotor.get("frontRight");
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);  // reverse direction of right motor
        frontRightMotor.setPower(0);
        rack.setPower(0);
        arm = hardwareMap.dcMotor.get("arm");
        arm.setPower(0);
/*
        // name the motors on REV Expansion 2 hub
        intake = hardwareMap.dcMotor.get("intake");
        extension = hardwareMap.dcMotor.get("extension");
        intake.setPower(0);
        extension.setPower(0);
        */
    }

    public void updateControllerState() {
        values[0] = gamepad1.left_stick_x;
        values[1] = gamepad1.right_stick_x;
        values[2] = gamepad1.left_trigger;
        values[3] = gamepad1.right_trigger;
        values[4] = convertBoolean(gamepad1.left_bumper);
        values[5] = convertBoolean(gamepad1.right_bumper);
        values[6] = convertBoolean(gamepad1.a);
        values[7] = convertBoolean(gamepad1.b);
        values[8] = convertBoolean(gamepad1.x);
        values[9] = convertBoolean(gamepad1.y);
        values[10] = convertBoolean(gamepad1.right_stick_button);
        values[11] = convertBoolean(gamepad1.left_stick_button);
        values[12] = convertBoolean(gamepad1.dpad_left);
        values[13] = convertBoolean(gamepad1.dpad_right);
        values[14] = convertBoolean(gamepad1.dpad_up);
        values[15] = convertBoolean(gamepad1.dpad_down);
        values[16] = gamepad1.left_stick_y;
        values[17] = gamepad1.right_stick_y;
    }

    // Converts booleans to floats (true = 1, false = 0)
    public double convertBoolean(boolean x){
        if(x)
            return 1;
        else
            return 0;
    }
    //controls are set here and algorithms for those controls
    public void setRobotState() {

        // Drive System Controls
        double r = Math.hypot(values[0], values[16]);
        double robotAngle = Math.atan2(values[16], values[0]) - Math.PI / 4;
        double rightX = values[1];
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        frontLeftMotor.setPower(v1);
        frontRightMotor.setPower(v2);
        leftMotor.setPower(v3);
        rightMotor.setPower(v4);


        //Rack controls values[4] /left bumper values[5] / right bumper
        double liftRack = 0.35;
        if(values[4] == 1) {
            rack.setPower(-liftRack);
        }
        if(values[5] == 1) {
            rack.setPower(liftRack);
        }
        if((values[4] == 0) && (values[5] == 0)) {
            rack.setPower(0);
        }
        if (values[4] == 1 && values[5] ==1) {
            rack.setPower(0);
        }



        /*     // Intake Controls values[6] /A values[7] /B
        // hold to take in objects
        double intakeSpeed = 1.0;
        if (values[6] == 1) {
            intake.setPower(intakeSpeed);
        }
        // hold to eject objects
        if (values[7] == 1) {
            intake.setPower(-intakeSpeed);
        }
        // do nothing when neither button is held
        if (values[6] == 0 && values[7] == 0) {
            intake.setPower(0);
        }
        if (values[6] == 1 && values[7] ==1){
            intake.setPower(0);
        }

        */

        // Extension Controls values[16] /LeftStickY
        // variables used
      /*  double extensionSpeed = 0.5;
        if (values[16] >= 0.1) {
            extension.setPower(extensionSpeed);
        }
        // hold to eject objects
        if (values[16] <= -0.1) {
            extension.setPower(-extensionSpeed);
        }
        // do nothing when neither button is held
        if (values[16] > -0.1 && values[16] <0.1) {
            extension.setPower(0);
        }
*/


        // Lift arm controls values[14] /DpadUp values[15] /DpadDown
        // variables used
        double armSpeed = .1;
        // Lift Drawer Slides
        if (values[14] == 1) {
            arm.setPower(armSpeed);
        }
        // Lower arm
        if (values[15] == 1) {
            arm.setPower(armSpeed);
        }
        // Hold Arm Still
        if (values[14] == 0 && values[15] == 0) {
            arm.setPower(0);
        }


    }


    public void loopCode() {
        updateControllerState();
        setRobotState();
    }
}
