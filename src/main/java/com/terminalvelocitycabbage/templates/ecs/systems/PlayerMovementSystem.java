package com.terminalvelocitycabbage.templates.ecs.systems;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.input.InputFrame;
import com.terminalvelocitycabbage.engine.ecs.ComponentFilter;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.utils.EasingUtil;
import com.terminalvelocitycabbage.templates.ecs.components.*;

import static com.terminalvelocitycabbage.engine.utils.EasingUtil.Direction.OUT;
import static com.terminalvelocitycabbage.engine.utils.EasingUtil.Function.CIRCULAR;

public abstract class PlayerMovementSystem extends InputHandlerSystem {

    protected int xInput = 0;
    protected int yInput = 0;
    protected int zInput = 0;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Entity player = ClientBase.getInstance().getManager().getFirstMatchingEntity(
                ComponentFilter
                        .builder()
                        .allOf(
                                CameraComponent.class,
                                PositionComponent.class,
                                PitchYawRotationComponent.class,
                                MovementSpeedComponent.class,
                                InputSensitivityComponent.class)
                        .build());

        float movementModifier = player.getComponent(MovementSpeedComponent.class).getSpeed();
        float inputSensitivity = player.getComponent(InputSensitivityComponent.class).getSensitivity();

        if (player.containsComponent(InputListenerComponent.class)) {

            InputFrame currentFrame = player.getComponent(InputListenerComponent.class).getCurrentInputFrame();

            //update the input stuff
            xInput = 0;
            yInput = 0;
            zInput = 0;
            updateDeltas(currentFrame);

            //Avoid division by 0
            if (deltaTime == 0) deltaTime += 0.01f;

            //Update Target Rotations
            if (currentFrame.isRightButtonPressed()) {
                modifyRotateTarget(player, currentFrame.getMouseDeltaY(), currentFrame.getMouseDeltaX(), deltaTime, inputSensitivity);
            }

            //Update Target Movement
            updateTargetPosition(player, deltaTime, currentFrame, movementModifier);
        }

        //Update the rotations
        updateRotations(player, deltaTime);

        //Update current rotations
        updatePosition(player, deltaTime, movementModifier);
    }

    public void modifyRotateTarget(Entity entity, float deltaVelocityX, float deltaVelocityY, float deltaTime, float rotateModifier) {
        entity.getComponent(PitchYawRotationTargetComponent.class).addRotateTarget(rotateModifier * deltaVelocityX / deltaTime, rotateModifier * deltaVelocityY / deltaTime);
        entity.getComponent(PitchYawRotationTargetComponent.class).setRotateProgress(0);
    }

    public void updateRotations(Entity entity, float deltaTime) {
        entity.getComponent(PitchYawRotationTargetComponent.class).addRotationProgress(5 / deltaTime);

        var rotateProgress = entity.getComponent(PitchYawRotationTargetComponent.class).getRotateProgress();
        var rotateTarget = entity.getComponent(PitchYawRotationTargetComponent.class).getRotateTarget();
        var currentRotation = entity.getComponent(PitchYawRotationComponent.class).getRotation();

        entity.getComponent(PitchYawRotationComponent.class).setRotation(
                EasingUtil.lerp(currentRotation.x, rotateTarget.x, rotateProgress, OUT, CIRCULAR),
                EasingUtil.lerp(currentRotation.y, rotateTarget.y, rotateProgress, OUT, CIRCULAR)
        );
    }

    //Implement this to update the changes from the input handler
    public abstract void updateDeltas(InputFrame currentFrame);

    public void updateTargetPosition(Entity entity, float deltaTime, InputFrame currentFrame, float moveModifier) {

        if (xInput != 0 || yInput != 0 || zInput != 0) entity.getComponent(PositionTargetComponent.class).setMoveProgress(0);

        float xMovement = moveModifier * xInput / deltaTime;
        float yMovement = moveModifier * yInput / deltaTime;
        float zMovement = moveModifier * zInput / deltaTime;

        var currentRotation = entity.getComponent(PitchYawRotationComponent.class).getRotation();

        entity.getComponent(PositionTargetComponent.class).addMoveTarget(
                ((float)Math.sin(currentRotation.y) * zMovement) + (float)Math.sin(currentRotation.y - Math.toRadians(90)) * xMovement,
                -yMovement,
                ((float)Math.cos(currentRotation.y) * -zMovement) + (float)Math.cos(currentRotation.y - Math.toRadians(90)) * -xMovement
        );
    }

    public void updatePosition(Entity entity, float deltaTime, float moveModifier) {
        entity.getComponent(PositionTargetComponent.class).addMoveProgress((moveModifier / deltaTime) / 2000f);

        var moveProgress = entity.getComponent(PositionTargetComponent.class).getMoveProgress();
        var moveTarget = entity.getComponent(PositionTargetComponent.class).getMoveTarget();
        var currentPosition = entity.getComponent(PositionComponent.class).getPosition();

        entity.getComponent(PositionComponent.class).setPosition(
                EasingUtil.lerp(currentPosition.x, moveTarget.x, moveProgress, OUT, CIRCULAR),
                EasingUtil.lerp(currentPosition.y, moveTarget.y, moveProgress, OUT, CIRCULAR),
                EasingUtil.lerp(currentPosition.z, moveTarget.z, moveProgress, OUT, CIRCULAR)
        );
    }
}
