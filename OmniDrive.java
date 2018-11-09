package org.firstinspires.ftc.teamcode;

/**
 * Created by Admin on 2018/10/9.
 */
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Omni Drive")
@Disabled

public class OmniDrive extends  LinearOpMode
{
    private DcMotor frontLeft, frontRight, rearLeft, rearRight, extend, intake;
    private Servo arm;
    GyroSensor gyro;

    double LfL, LfR, LrL, LrR;
    double goLine, goTurn, leftMult, rightMult;
    double hmfL, hmfR, hmrL, hmrR;

    double intakeMult;
    double extendArm = 0;

    double motorLimitMult = -0.5;

    //declare encoder test
    double encoderRunTest = 0;


    public void runOpMode() throws InterruptedException
    {
        initialize();

        waitForStart();
        while(opModeIsActive())
        {
            loopCode();
        }
    }

    public void initialize() throws InterruptedException
    {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        rearLeft = hardwareMap.dcMotor.get("rearLeft");
        rearRight = hardwareMap.dcMotor.get("rearRight");
        gyro = hardwareMap.gyroSensor.get("gyro");

        // name the lift and intake item motors
        extend = hardwareMap.dcMotor.get("extend");
        intake = hardwareMap.dcMotor.get("intake");

        // name the servos
        arm = hardwareMap.servo.get("arm");
        arm.setPosition(0);
    }

    public void loopCode() throws InterruptedException
    { // to intake the items
        intakeMult = -gamepad1.right_trigger * 1;

        // to lift the arm up and back
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

        if((gamepad1.left_stick_x != 0)||(gamepad1.left_stick_y != 0))
        {
            leftMult = 0;
            rightMult = 0;
        }
        else
        {
            goTurn = gamepad1.right_stick_x;
            goLine = -gamepad1.right_stick_y;
            leftMult = goLine + goTurn;
            rightMult = goLine - goTurn;

        }

        //++ -> hmF + hmR > 1.5
        //-- -> hmF + hmR < -1.5
        //-+  & +- -> hmF + hmR ~= 0
        //Multiplier for controls is 4/3
        //For better results, take average of 2 controls, times 4/3, then divide by 2(or divide by 2 then multiply)
        // Note: Y AXIS IS FLIPPED!!!

        if((gamepad1.right_stick_x != 0) || (gamepad1.right_stick_y != 0))
        {
            LfL = 0;
            LfR = 0;
            LrL = 0;
            LrR = 0;
        }
        else
        {
            if((Math.abs(gamepad1.left_stick_x) > 0) && (Math.abs(gamepad1.left_stick_y) < 0.1))
            {
                //strafe across
                //to right, front negative; to left, rear negative
                //-fL,-fR,-rL,-rR
                LfL = -gamepad1.left_stick_x;
                LfR = -gamepad1.left_stick_x;
                LrL = gamepad1.left_stick_x;
                LrR = gamepad1.left_stick_x;
            }
            else if((gamepad1.left_stick_x + gamepad1.left_stick_y > 1.25)||(gamepad1.left_stick_x + gamepad1.left_stick_y < -1.25)) //check,cannot identify
            {
                if((gamepad1.left_stick_x > 0)||(gamepad1.left_stick_y > 0))
                {
                    //bottom right corner, both vals ++
                    //+rL,-fR
                    LfL = 0;
                    LfR = -(((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LrL = (((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LrR = 0;
                }
                else if((gamepad1.left_stick_x < 0)||(gamepad1.left_stick_y < 0))
                {
                    //top left corner both vals --
                    //-rL,+fR
                    LfL = 0;
                    LfR = (((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LrL = -(((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LrR = 0;
                }
            }
            else if(Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y) > 0)//check, direction changes at angle
            {
                if((gamepad1.left_stick_x > 0)||(gamepad1.left_stick_y < 0))
                {
                    //top right corner
                    //-fL && +rR
                    LfL = -(((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LfR = 0;
                    LrL = 0;
                    LrR = (((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                }
                else if((gamepad1.left_stick_x < 0)||(gamepad1.left_stick_y > 0))
                {
                    //bottom left corner
                    //+fl && -rR
                    LfL = (((Math.abs(gamepad1.left_stick_x + gamepad1.left_stick_y))/2)*4/3);
                    LfR = 0;
                    LrL = 0;
                    LrR = -(((Math.abs(gamepad1.left_stick_x+gamepad1.left_stick_y))/2)*4/3);
                }
            }
            else
            {
                //set multipliers 0 or 1
                LfL = 0;
                LfR = 0;
                LrL = 0;
                LrR = 0;
            }
        }

        scale(LfL);
        scale(LfR);
        scale(LrL);
        scale(LrR);

        //left joystick x axis = strafe
        //diagonal = diagonal movement

        hmfL = (-leftMult + LfL);
        hmrL = (-leftMult + LrL);
        hmfR = (rightMult + LfR);
        hmrR = (rightMult + LrR);

        scale(hmfL);
        scale(hmrL);
        scale(hmfR);
        scale(hmrR);

        frontLeft.setPower(hmfL);
        rearLeft.setPower(hmrL);
        frontRight.setPower(hmfR);
        rearRight.setPower(hmrR);
        intake.setPower(intakeMult);
        extend.setPower(extendArm);
    }

    public double scale(double i)
    {
        double v = i;
        v = Range.clip(v,-1,1);
        return v;
    }
}
