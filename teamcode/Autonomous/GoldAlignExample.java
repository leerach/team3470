/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Autonomous;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name="GoldAlign Example", group="DogeCV")

public class GoldAlignExample extends LinearOpMode {
    private GoldAlignDetector detector;
    private DcMotor rack;
    private DcMotor frontLeft;
    private DcMotor left;
    private DcMotor frontRight;
    private DcMotor right;
    private DcMotor arm;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");


        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), 1, false);
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

        detector.enable();

        rack = hardwareMap.dcMotor.get("rack");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        left = hardwareMap.dcMotor.get("left");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        right = hardwareMap.dcMotor.get("right");
        arm = hardwareMap.dcMotor.get("arm");



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
            moveForward(.2, 400);
            sleep(200);
            moveRight(.2, 200);
            sleep(200);
            moveForward(.2, 500);
            sleep(200);
            // time taken to move gold block
            int pushTime = 1100;
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
            arm.setPower(-0.75);
            sleep(250);
            arm.setPower(0);


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
