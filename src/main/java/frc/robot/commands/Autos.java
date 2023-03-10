// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.List;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.Constants.SwerveConstants;

public final class Autos {
  private static PIDController frontController = new PIDController(SwerveConstants.AUTO_kP_FRONT, 0, 0);
  private static PIDController sideController = new PIDController(SwerveConstants.AUTO_kP_SIDE, 0, 0);
  private static PIDController turnController = new PIDController(SwerveConstants.AUTO_kP_TURN, 0, 0);

  /** Example static factory for an autonomous command. */
  public static CommandBase c1C0_M_Bal() {
    PathPlannerTrajectory trajectory = PathPlanner.loadPath("c1C0_M_Bal", SwerveConstants.AUTO_DRIVE_MAX_SPEED / 2.25, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION);

    PPSwerveControllerCommand driveOnCS = makeSwerveControllerCommand(trajectory);

    return new SequentialCommandGroup(
      new InstantCommand(() -> RobotContainer.drivetrain.resetOdometry(getInitialPose(trajectory))),
      new InstantCommand(() -> RobotContainer.drivetrain.setAllMode(true)),
      new InstantCommand(() -> RobotContainer.shooter.setHighMode()),
      new WaitCommand(1),
      new Shoot(0.75).withTimeout(1),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      driveOnCS,
      new AutoBalance(),
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules())
    );
  }

  public static CommandBase c2C0_NC() {
    List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup("c2C0_NC", 
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION));

    PPSwerveControllerCommand driveToCube1 = makeSwerveControllerCommand(pathGroup.get(0));
    
    return Commands.sequence(
      new InstantCommand(() -> RobotContainer.drivetrain.resetOdometry(getInitialPose(pathGroup.get(0)))),
      new InstantCommand(() -> RobotContainer.drivetrain.setAllMode(true)),
      new InstantCommand(() -> RobotContainer.shooter.setHighMode()),
      new Shoot(0.75).withTimeout(1.25),
      new InstantCommand(() -> RobotContainer.shooter.shooterOff()),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      new InstantCommand(() -> RobotContainer.intake.intakeToggle()),
      new InstantCommand(() -> RobotContainer.shooter.setMidMode()),
      driveToCube1,
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules()),
      new Shoot(0.75).withTimeout(1.25),
      new InstantCommand(() -> RobotContainer.shooter.shooterOff()),
      new InstantCommand(() -> RobotContainer.transport.feederStop())
    );
  }

  public static CommandBase c2C0_NC_Bal() {
    List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup("c2C0_NC_Bal", 
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION),
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED / 1.5, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION));

    PPSwerveControllerCommand driveToCube1 = makeSwerveControllerCommand(pathGroup.get(0));
    PPSwerveControllerCommand driveOnCS = makeSwerveControllerCommand(pathGroup.get(1));
    
    return Commands.sequence(
      new InstantCommand(() -> RobotContainer.drivetrain.resetOdometry(getInitialPose(pathGroup.get(0)))),
      new InstantCommand(() -> RobotContainer.drivetrain.setAllMode(true)),
      new InstantCommand(() -> RobotContainer.shooter.setHighMode()),
      new Shoot(0.75).withTimeout(1.25),
      new InstantCommand(() -> RobotContainer.shooter.shooterOff()),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      new InstantCommand(() -> RobotContainer.intake.intakeToggle()),
      new InstantCommand(() -> RobotContainer.shooter.setCSMode()),
      driveToCube1,
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules()),
      new Shoot(0.75).withTimeout(1.25),
      new InstantCommand(() -> RobotContainer.shooter.shooterOff()),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      driveOnCS,
      new AutoBalance(),
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules())
    );
  }

  public static CommandBase c3C0_NC_Bal() {
    List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup("c3C0_NC_Bal", 
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION),
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION),
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED / 1.5, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION));

    PPSwerveControllerCommand driveToCube1 = makeSwerveControllerCommand(pathGroup.get(0));
    PPSwerveControllerCommand driveToCube2 = makeSwerveControllerCommand(pathGroup.get(1));
    PPSwerveControllerCommand driveOnCS = makeSwerveControllerCommand(pathGroup.get(2));
    
    return Commands.sequence(
      new InstantCommand(() -> RobotContainer.drivetrain.resetOdometry(getInitialPose(pathGroup.get(0)))),
      new InstantCommand(() -> RobotContainer.drivetrain.setAllMode(true)),
      new InstantCommand(() -> RobotContainer.shooter.setHighMode()),
      new Shoot(0.75).withTimeout(1.25),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      new InstantCommand(() -> RobotContainer.intake.intakeToggle()),
      new InstantCommand(() -> RobotContainer.shooter.setCSMode()),
      driveToCube1,
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules()),
      new Shoot(0.6).withTimeout(1.1),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      driveToCube2,
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules()),
      new Shoot(0).withTimeout(0.5),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      driveOnCS,
      new AutoBalance(),
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules()),
      new InstantCommand(() -> RobotContainer.shooter.shooterOff())
    );
  }

  public static CommandBase c4C0_NC() {
    List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup("c4C0_NC", 
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION),
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION),
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION),
      new PathConstraints(SwerveConstants.AUTO_DRIVE_MAX_SPEED * 1.5, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION));

    PPSwerveControllerCommand driveToCube1 = makeSwerveControllerCommand(pathGroup.get(0));
    PPSwerveControllerCommand driveToCube2 = makeSwerveControllerCommand(pathGroup.get(1));
    PPSwerveControllerCommand driveToCube3 = makeSwerveControllerCommand(pathGroup.get(2));
    PPSwerveControllerCommand driveToCube4 = makeSwerveControllerCommand(pathGroup.get(3));
    
    return Commands.sequence(
      new InstantCommand(() -> RobotContainer.drivetrain.resetOdometry(getInitialPose(pathGroup.get(0)))),
      new InstantCommand(() -> RobotContainer.drivetrain.setAllMode(true)),
      new InstantCommand(() -> RobotContainer.shooter.setHighMode()),
      new Shoot(0.75).withTimeout(1.25),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      new InstantCommand(() -> RobotContainer.intake.intakeToggle()),
      new InstantCommand(() -> RobotContainer.shooter.setCSMode()),
      new InstantCommand(() -> RobotContainer.shooter.shooterHold()),
      driveToCube1,
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules()),
      new Shoot(0).withTimeout(0.5),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      driveToCube2,
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules()),
      new Shoot(0).withTimeout(0.5),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      driveToCube3,
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules()),
      new Shoot(0).withTimeout(0.5),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      new InstantCommand(() -> RobotContainer.shooter.shooterOff()),
      driveToCube4,
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules())
    );
  }

  public static CommandBase driveBack() {
    PathPlannerTrajectory trajectory = PathPlanner.loadPath("DriveBack", SwerveConstants.AUTO_DRIVE_MAX_SPEED, SwerveConstants.AUTO_DRIVE_MAX_ACCELERATION);

    PPSwerveControllerCommand driveBack = makeSwerveControllerCommand(trajectory);

    return new SequentialCommandGroup(
      new InstantCommand(() -> RobotContainer.drivetrain.resetOdometry(getInitialPose(trajectory))),
      new InstantCommand(() -> RobotContainer.drivetrain.setAllMode(true)),
      new InstantCommand(() -> RobotContainer.shooter.shooterOff()),
      new InstantCommand(() -> RobotContainer.transport.feederStop()),
      driveBack,
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules())
    );
  }

  public static CommandBase testAuto(){
    return new SequentialCommandGroup(
      new InstantCommand(() -> RobotContainer.drivetrain.stopModules())
    );
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }

  private static PPSwerveControllerCommand makeSwerveControllerCommand(PathPlannerTrajectory traj){
    turnController.enableContinuousInput(-Math.PI, Math.PI);
    return new PPSwerveControllerCommand(
      traj, 
      RobotContainer.drivetrain::getPose, 
      SwerveConstants.DRIVE_KINEMATICS,
      frontController, 
      sideController,
      turnController, 
      RobotContainer.drivetrain::setModuleStates,
      true,
      RobotContainer.drivetrain);
  }

  private static Pose2d getInitialPose(PathPlannerTrajectory traj){
    PathPlannerState initialState = DriverStation.getAlliance().equals(Alliance.Red) ? 
    PathPlannerTrajectory.transformStateForAlliance(traj.getInitialState(), Alliance.Red) :
    traj.getInitialState();

    return new Pose2d(initialState.poseMeters.getTranslation(), initialState.holonomicRotation);
  }
}
