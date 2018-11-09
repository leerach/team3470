
package org.firstinspires.ftc.teamcode;
        import com.disnodeteam.dogecv.CameraViewDisplay;
        import com.disnodeteam.dogecv.DogeCV;
        import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.Servo;

        import org.firstinspires.ftc.robotcore.external.ClassFactory;
        import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
        import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
        import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
        import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

        import com.disnodeteam.dogecv.CameraViewDisplay;
        import com.disnodeteam.dogecv.DogeCV;
        import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
        import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Rachel on 10/13/2018.
 */

@Autonomous(name = "Crater Autonomous")

public class CraterAuto extends LinearOpMode{

    private GoldAlignDetector detector;

    //declare motors
    private DcMotor frontLeft, frontRight, rearLeft, rearRight, extend, intake;
    private Servo arm;

    // declare variables for Mecanum wheel drive movement (left, right, diagonal, forward etc.)
    double LeftFrontLeft, LeftFrontRight, LeftRearLeft, LeftRearRight;
    double leftValue, rightValue;
    double hmfL, hmfR, hmrL, hmrR;
    double intakeMult;
    double extendArm = 0;
    double xPosOfBlock = detector.getXPosition();

    //-2400 for 90 degree turn
    final double counterClockwiseTurn = -2440;
    final double motorPower = 0.1;
    final double motorPowerSlow = 0.2;

    boolean blockFound = false;
    boolean blockAligned = detector.getAligned();

    @Override
    public void runOpMode() throws InterruptedException
    {
        // initialize motors
        frontLeft = hardwareMap.dcMotor.get("fl");
        frontRight = hardwareMap.dcMotor.get("fr");
        rearLeft = hardwareMap.dcMotor.get("rl");
        rearRight = hardwareMap.dcMotor.get("rr");

        // name the lift and intake item motors
        extend = hardwareMap.dcMotor.get("extend");
        intake = hardwareMap.dcMotor.get("intake");

        // name the servos
        arm = hardwareMap.servo.get("arm");
        arm.setPosition(0);

        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");

        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        waitForStart();

        detector.enable();

        while(opModeIsActive())
        {
            //detach();
            dataLoop();
            driveToBlock();
            turnCounterClockwise(motorPowerSlow);
            

            telemetry.update();

            detector.disable();
            allStop();
        }


    }

    public void dataLoop() {
        telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral
        telemetry.addData("X Pos" , detector.getXPosition()); // Gold X pos.
    }

    public void driveToBlock() {

        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (blockAligned)
            while (blockAligned) {
                goYdirection(motorPower);
            }
        else
            while((rearLeft.getCurrentPosition() + rearRight.getCurrentPosition())/2 < xPosOfBlock){
            goXdirection(motorPowerSlow);
            telemetry.update();
            }
            while (rearLeft.getCurrentPosition() + rearRight.getCurrentPosition()/2 > xPosOfBlock){
            goXdirection(-motorPowerSlow);
            telemetry.update();
            }
    }
   /* public void detach(){

    }
    */

    public void allStop() {
        frontLeft.setPower(0);
        rearLeft.setPower(0);
        frontRight.setPower(0);
        rearRight.setPower(0);
    }

    public void goXdirection(double powermotor) {  // x axis direction move: power>0 x axis postive ;power<0, x axis negative
        frontLeft.setPower(-powermotor);
        rearLeft.setPower(powermotor);
        frontRight.setPower(-powermotor);
        rearRight.setPower(powermotor);
    }

    public void goYdirection(double powermotor) { // Y axis direction move: Power <0 yaxis postion; power >0,Y axis negative
        frontLeft.setPower(powermotor);
        rearLeft.setPower(powermotor);
        frontRight.setPower(-powermotor);
        rearRight.setPower(-powermotor);
    }

    public void turnCounterClockwise(double powermotor) {
        frontLeft.setPower(-powermotor);
        rearLeft.setPower(-powermotor);
        frontRight.setPower(-powermotor);
        rearRight.setPower(-powermotor);
    }
}
