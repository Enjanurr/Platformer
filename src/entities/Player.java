package entities;

import main.Game;
import utils.LoadSave;


import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;


public class Player extends  Entity{
   private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;

    // Player state
    private int playerAction = IDLE;
    private boolean left, up, right, down,jump;
    private float playerSpeed = 2.0f;
    private boolean moving = false, attacking = false;
    private int[][] levelData;
    private float xDrawOffSet = 21 * Game.SCALE;
    private float yDrawOffSet = 4 * Game.SCALE;

    // Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;

    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;


    public Player(float x, float y, int width , int height) {
        super(x, y, width , height);
        loadAnimation();
        initHitbox(x,y,20 * Game.SCALE,27 * Game.SCALE);

    }
    public void update(){
        updatePosition();
        updateAnimationTick();
        setAnimation();


    }
    public void render(Graphics g){
        g.drawImage(
                animations[playerAction][aniIndex],
                (int)(hitbox.x - xDrawOffSet),
                (int) (hitbox.y - yDrawOffSet),
                width,
                height,
                null
        );
        //drawHitbox(g);
    }




    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }
    private void setAnimation() {
        int startAni = playerAction;
        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if(inAir){
        if(airSpeed < 0){
            playerAction = JUMP;
        }else{
            playerAction = FALLING;
        }
        }

        if(attacking){
            playerAction = ATTACK_1;
        }
        if(startAni != playerAction){
            resetAniTick();
        }
    }

    // for the attack so it won't slop when attacking because of the sprites
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePosition() {
        moving = false;
        if(jump){
            jump();
            jump =  false;
        }
        if (!left && !right && !inAir)
            return;

        float xSpeed = 0;

        if (left)
            xSpeed -= playerSpeed;
        if (right)
            xSpeed += playerSpeed;

        if(!inAir){
            if(!isEntityOnFloor(hitbox,levelData)){
                inAir = true;
            }
        }

        if(inAir){
         if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width,hitbox.height, levelData )){
             hitbox.y += airSpeed;
             airSpeed += gravity;
             updateXPos(xSpeed);
         }else{
             hitbox.y = GetEntityXPosUnderRoofOrAboveFloor(hitbox,airSpeed);
             if(airSpeed > 0){
                 resetInAir();
             }else{
                 airSpeed = fallSpeedAfterCollision;
             }
             updateXPos(xSpeed);
         }
        }else{
            updateXPos(xSpeed);
        }
        moving = true;

    }

    private void jump() {
        if(inAir){
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
                if (canMoveHere(hitbox.x + xSpeed, hitbox.y , hitbox.width, hitbox.height, levelData)) {
            hitbox.x += xSpeed;
        }else{
                    hitbox.x =  GetEntityXPosNextToWall(hitbox, xSpeed);
                }
    }


    // load the animation of the player
    private void loadAnimation() {

          BufferedImage  img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

           animations = new BufferedImage[9][6];
            for (int j = 0; j < animations.length; j++) {          // animation row (action)
                for (int i = 0; i < animations[j].length; i++) {   // frame index
                    animations[j][i] = img.getSubimage(
                            i * 64,    // x
                            j * 40,    // y
                            64,        // width
                            40         // height
                    );
                }
            }
    }

    public void loadLevelData(int[][] levelData){
        this.levelData = levelData;
        if(!isEntityOnFloor(hitbox,levelData)){
            inAir = true;
        }
    }

    //  for the controls
    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
    public void setJump(boolean jump){
        this.jump = jump;
    }
    public boolean isJump() {
        return jump;
    }


    // stabilizer so character wont bug if window is closed , see GameWindow class
    public void resetDirBooleans(){
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking){
    this.attacking = attacking;
    }
}
