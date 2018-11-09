package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Rachel on 9/28/2018.
 */
@TeleOp(name = "Mecanum Drive")
public class MecanumDrive extends LinearOpMode {

    // declare motors
    private DcMotor frontLeft, frontRight, rearLeft, rearRight;
    private Servo
    //extend, intake;
    //private Servo arm;

    // declare variables for Mecanum wheel drive movement (left, right, diagonal, forward etc.)
    double LeftFrontLeft, LeftFrontRight, LeftRearLeft, LeftRearRight;
    double leftValue, rightValue;
    double hmfL, hmfR, hmrL, hmrR;
  //  double intakeMult;
    double extendArm = 0;

    double motorLimitMult = -0.5;

    //declare encoder test
    double encoderRunTest = 0;

    @Override
    public void runOpMode() throws InterruptedException
    {
        // initialize motors
        frontLeft = hardwareMap.dcMotor.get("fl");
        frontRight = hardwareMap.dcMotor.get("fr");
        rearLeft = hardwareMap.dcMotor.get("rl");
        rearRight = hardwareMap.dcMotor.get("rr");

        //reverse motors to match Y-axis in left joystick so forward on joystick moves robot forward
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);


      // name the lift and intake item motors

             //  intake = hardwareMap.dcMotor.get("intake");

        // name the servos
        arm = hardwareMap.servo.get("arm");
        arm.setPosition(0);

        waitForStart();

        while(opModeIsActive())
        {
            loopedDrive();
        }
    }

    private void loopedDrive() {
        // to intake the items
        //intakeMult = -gamepad1.right_trigger * 1;

        // to lift the arm up and back
       /*
        if(gamepad1.dpad_up)
        {
            arm.setPosition(1);
        }
        else if(gamepad1.dpad_down)
        {
            arm.setPosition(0);
        }

        // to extend arm (adjust numbers as needed)
        if(gamepad1.left_bumper)
        {
            extendArm = 0.3;
        }
        else if(gamepad1.right_bumper)
        {
            extendArm = -0.3;
        }
        else
        {
            extendArm = 0;
        }

        encoderRunTest = rearLeft.getCurrentPosition();
*/
        //
        if((gamepad1.left_stick_x != 0)||(gamepad1.left_stick_y != 0))
        {
            leftValue = 0;
            rightValue = 0;
        }
        else
        {
            leftValue = -gamepad1.right_stick_y + gamepad1.right_stick_x;
            rightValue = -gamepad1.right_stick_y - gamepad1.right_stick_x;

        }

        //++ -> hmF + hmR > 1.5
        //-- -> hmF + hmR < -1.5
        //-+  & +- -> hmF + hmR ~= 0
        //Multiplier for controls is 4/3
        //For better results, take average of 2 controls, times 4/3, then divide by 2(or divide by 2 then multiply)
        // Note: Y AXIS IS FLIPPED!!!

        if((gamepad1.right_stick_x != 0) || (gamepad1.right_stick_y != 0))
        {
            LeftFrontLeft = 0;
            LeftFrontRight = 0;
            LeftRearLeft = 0;
            LeftRearRight = 0;
        }
        else
        {
            if((Math.abs(gamepad1.left_stick_x) > 0) && (Math.abs(gamepad1.left_stick_y) < 0.1))
            {
                //strafe across
                //to right, front negative; to left, rear negative
                //-fL,-fR,-rL,-rR
                LeftFrontLeft = -gamepad1.left_stick_x;
                LeftFrontRight = -gamepad1.left_stick_x;
                LeftRearLeft = gamepad1.left_stick_x;
                LeftRearRight = gamepad1.left_stick_x;
            }
            else if((gamepad1.left_stick_x + gamepad1.left_stick_y > 1.25)||(gamepad1.left_stick_x + gamepad1.left_stick_y < -1.25)) //check,cannot identify
            {
                if((gamepad1.left_stick_x > 0)||(gamepad1.left_stick_y > 0))
                {
                    //bottom right corner, both vals ++
                    //+rL,-fR
                    LeftFrontLeft = 0;
                    LeftFrontRight = -(((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LeftRearLeft = (((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LeftRearRight = 0;
                }
                else if((gamepad1.left_stick_x < 0)||(gamepad1.left_stick_y < 0))
                {
                    //top left corner both vals --
                    //-rL,+fR
                    LeftFrontLeft = 0;
                    LeftFrontRight = (((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LeftRearLeft = -(((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LeftRearRight = 0;
                }
            }
            else if(Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y) > 0)//check, direction changes at angle
            {
                if((gamepad1.left_stick_x > 0)||(gamepad1.left_stick_y < 0))
                {
                    //top right corner
                    //-fL && +rR
                    LeftFrontLeft = -(((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LeftFrontRight = 0;
                    LeftRearLeft = 0;
                    LeftRearRight = (((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                }
                else if((gamepad1.left_stick_x < 0)||(gamepad1.left_stick_y > 0))
                {
                    //bottom left corner
                    //+fl && -rR
                    LeftFrontLeft = (((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LeftFrontRight = 0;
                    LeftRearLeft = 0;
                    LeftRearRight = -(((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                }
            }
            else
            {
                //set multipliers 0 or 1
                LeftFrontLeft = 0;
                LeftFrontRight = 0;
                LeftRearLeft = 0;
                LeftRearRight = 0;
            }
        }

        scale(LeftFrontLeft);
        scale(LeftFrontRight);
        scale(LeftRearLeft);
        scale(LeftRearRight);

        //left joystick x axis = strafe
        //diagonal = diagonal movement
        hmfL = (-leftValue + LeftFrontLeft);
        hmrL = (-leftValue + LeftRearLeft);
        hmfR = (rightValue + LeftFrontRight);
        hmrR = (rightValue + LeftRearRight);

        scale(hmfL);
        scale(hmrL);
        scale(hmfR);
        scale(hmrR);

        frontLeft.setPower(hmfL*motorLimitMult);
        rearLeft.setPower(hmrL*motorLimitMult);
        frontRight.setPower(hmfR*motorLimitMult);
        rearRight.setPower(hmrR*motorLimitMult);

       // intake.setPower(intakeMult);
       // extend.setPower(extendArm);

        if(gamepad1.b)
        {
            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            while(rearLeft.getCurrentPosition() < 500)
            {
                frontLeft.setPower(0.05);
                rearLeft.setPower(0.05);
                frontRight.setPower(-0.05);
                rearRight.setPower(-0.05);

            }
        }

        telemetry.addData("FrontLeft", hmfL*motorLimitMult);
        telemetry.addData("RearLeft", hmrL*motorLimitMult);
        telemetry.addData("FrontRight", hmfR*motorLimitMult);
        telemetry.addData("RearRight", hmrR*motorLimitMult);
        telemetry.addData("Encoder Return:", encoderRunTest);
        telemetry.update();
    }

    public double scale(double i)
    {
        double v = i;
        v = Range.clip(v, -1, 1);
        return v;
    }
}