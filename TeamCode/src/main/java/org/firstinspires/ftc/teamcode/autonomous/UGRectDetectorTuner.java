package org.firstinspires.ftc.teamcode.autonomous;

import com.arcrobotics.ftclib.vision.UGRectDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Rect Detector Tuner", group="test")
public class UGRectDetectorTuner extends LinearOpMode {

    static double topRectangleWidthPerc = 0.25;
    static double topRectangleHeightPerc = 0.25;

    static double bottomRectangleWidthPerc = 0.25;
    static double bottomRectangleHeightPerc = 0.25;

    static int rectangleWidth = 10;
    static int rectangleHeight = 10;

    static double joystickSensitivity = 0.0005;

    @Override
    public void runOpMode() {

        UGRectDetector detector = new UGRectDetector(hardwareMap);

        telemetry.addData("[>]", "Press start to begin tuning");
        telemetry.update();

        waitForStart();

        detector.init();

        while(!isStopRequested()) {

            double topRectanglePlusX = gamepad1.left_stick_x * joystickSensitivity;
            double topRectanglePlusY = -gamepad1.left_stick_y * joystickSensitivity;
            double bottomRectanglePlusX = gamepad1.right_stick_x * joystickSensitivity;
            double bottomRectanglePlusY = -gamepad1.right_stick_y * joystickSensitivity;

            topRectangleWidthPerc += topRectanglePlusX;
            topRectangleHeightPerc += topRectanglePlusY;
            bottomRectangleWidthPerc += bottomRectanglePlusX;
            bottomRectangleHeightPerc += bottomRectanglePlusY;

            if(gamepad1.dpad_left) {
                rectangleWidth -= 1;
            } else if(gamepad1.dpad_right) {
                rectangleHeight += 1;
            }

            if(gamepad1.dpad_up) {
                rectangleHeight += 1;
            } else if(gamepad1.dpad_down) {
                rectangleHeight -= 1;
            }

            detector.setTopRectangle(topRectangleHeightPerc, topRectangleWidthPerc);
            detector.setBottomRectangle(bottomRectangleHeightPerc, bottomRectangleWidthPerc);

            detector.setRectangleSize(rectangleWidth, rectangleHeight);

            telemetry.addData("[Data Order]", "Height, Weight");

            telemetry.addData("[Top Rect%]", "%.2f %.2f", topRectangleWidthPerc, topRectangleHeightPerc);
            telemetry.addData("[Bottom Rect%]", "%.2f %.2f", bottomRectangleWidthPerc, bottomRectangleHeightPerc);
            telemetry.addData("[Rectangle Size(PX)]", "%.2f %.2f", rectangleWidth, rectangleHeight);

            telemetry.update();

        }

    }

}
