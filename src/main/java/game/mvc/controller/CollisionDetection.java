package game.mvc.controller;

import game.mvc.model.*;
import game.mvc.model.Character;
import game.mvc.model.*;

public class CollisionDetection {
    /*
    When determine collision, enemies has three circles:
    1. body circle: used to determine whether be hit by the katana; and
        when to enter the attack mode
    2. view circle: used to determine whether it can see the character and start to chase
    3. attack circle: used to determine whether it can hit the character
     */

    public static int runSoundIdx = 1;

    public static void checkAllCollisions() {
        checkPlayerAttackResult();
        checkEnemyAttackResult();
        checkEnemyViewRange();
    }

    /**
     * check if character's attack hit some enemies.
     * only valid if:
     *      1. character is attacking, and katana's circle overlap with some enemies' body circle
     *      2. character is attacking, and katana's circle overlap with some bullets' cycle
     */
    public static void checkPlayerAttackResult() {
        Zero zero = CommandCenter.getInstance().getZero();
        if (zero.isAttack()) {

            // only in attack action we check the collision
            // between katana and enemies
            Katana currentKatana = zero.getKatana();
            int katanaRadius = currentKatana.getRadius();
            int enemyRadius = Character.MIN_RADIUS;
            for (Movable enemy: CommandCenter.getInstance().getMovEnemies()) {
                if (currentKatana.getCenter().distance(enemy.getCenter()) < (katanaRadius + enemyRadius)) {

                    // only detect enemies that are not protected,
                    // in other words they are alive
                    if (!enemy.isProtected()) {

                        // if there are no hit debris then create one. To ensure at the same time there
                        // is at most one hit debris even multiple enemies get hurt at the same time
                        if (currentKatana.getHitSlashDebris() == null) {
                            HitSlashDebris hitSlashDebris = new HitSlashDebris(currentKatana.getCenter(), enemy.getCenter());
                            currentKatana.setHitSlashDebris(hitSlashDebris);
                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    hitSlashDebris,
                                    GameOp.Action.ADD
                            );
                        }

                        // perform enemy hurt by katana's action
                        ((Character) enemy).getHurt(currentKatana);
                        Sound.playSound(String.format("Enemy/sound_enemy_death_sword_0%d.wav", Game.R.nextInt(2) + 1));
                        Sound.playSound("Enemy/sound_enemy_death_generic.wav");
                        CommandCenter.getInstance().setEnemyNums(CommandCenter.getInstance().getEnemyNums() - 1);
                        CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + 100);
                    }
                }
            }

            // detect if there are collision between katana and bullets
            for (Movable bullet: CommandCenter.getInstance().getMovBullets()) {
                Bullet bulletObj = (Bullet) bullet;
                if (!bulletObj.isReflected()) {
                    if (currentKatana.getCenter().distance(bullet.getCenter()) < (katanaRadius + bullet.getRadius())) {

                        // detect a collision
                        if (currentKatana.getBulletReflectionDebris() == null) {

                            // if there are no reflection debris, then create one. Ensure there are
                            // not multiple ones at the same time
                            BulletReflectionDebris bulletReflectionDebris = new BulletReflectionDebris(currentKatana.getCenter());
                            currentKatana.setBulletReflectionDebris(bulletReflectionDebris);
                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    bulletReflectionDebris,
                                    GameOp.Action.ADD
                            );
                        }

                        // perform corresponding action, and set reflected bullet
                        // a higher speed
                        Sound.playSound("Bullet/slash_bullet.wav");
                        bulletObj.setReflected(true);
                        bulletObj.setYVelocity(-bulletObj.getYVelocity());
                        bulletObj.setXVelocity(-bulletObj.getXVelocity() * 3);
                    }
                }
            }
        }
    }


    /**
     * This method check the collision between enemies' punch / bullet with
     * our player, and the collision result between bullet with enemies and bricks
     */
    public static void checkEnemyAttackResult() {
        Zero zero = CommandCenter.getInstance().getZero();

        // check if player is hit by punch
        if (!zero.isProtected()) {
            for (Movable punch: CommandCenter.getInstance().getMovPunches()) {
                if (punch.getCenter().distance(zero.getCenter()) < (punch.getRadius() + zero.getRadius())) {
                    zero.getHurt((Punch) punch);
                }
            }
        }

        // check if player is hit by bullet
        for (Movable bullet: CommandCenter.getInstance().getMovBullets()) {
            Bullet bulletObj = (Bullet) bullet;
            if (!bulletObj.isReflected()) {
                if (!zero.isProtected()) {
                    if (bulletObj.getCenter().distance(zero.getCenter()) < (bulletObj.getRadius() + zero.getRadius())) {
                        zero.getHurt(bulletObj);
                        CommandCenter.getInstance().getOpsQueue().enqueue(bullet, GameOp.Action.REMOVE);
                    }
                }
            } else {

                // it is a reflected bullet that should check collision with enemies
                for (Movable enemy: CommandCenter.getInstance().getMovEnemies()) {
                    if (bulletObj.getCenter().distance(enemy.getCenter()) < (enemy.getRadius() + bulletObj.getRadius())) {
                        if (!enemy.isProtected()) {

                            // perform enemy hurt by bullet's action
                            CommandCenter.getInstance().getOpsQueue().enqueue(bullet, GameOp.Action.REMOVE);
                            ((Character) enemy).getHurt(bulletObj);
                            Sound.playSound("Bullet/sound_enemy_death_bullet.wav");
                            Sound.playSound("Enemy/sound_enemy_death_generic.wav");
                            CommandCenter.getInstance().setEnemyNums(CommandCenter.getInstance().getEnemyNums() - 1);
                            CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + 100);
                        }
                    }
                }
            }
        }

        // check if bullet is hit on brick, and bullet
        // would vanish after hitting the bricks
        for (Movable bullet: CommandCenter.getInstance().getMovBullets()) {
            for (Movable block: CommandCenter.getInstance().getMovFloors()) {
                if (bullet.getCenter().distance(block.getCenter()) < (bullet.getRadius() + block.getRadius())) {
                    CommandCenter.getInstance().getOpsQueue().enqueue(bullet, GameOp.Action.REMOVE);
                }
            }
        }
    }

    /**
     * This method checks if our player enters the enemies' view cycle. If the player
     * enter the view cycle but not in the attack cycle, the enemy enter the 'chasing'
     * mode and would run towards the player's direction. If the player enter the enemy's
     * attack cycle, then they would perform attack action. For Grunt and Pomp this would
     * be a hit by punch, for Gangster and ShieldCop this attack would be shooting
     */
    public static void checkEnemyViewRange() {
        Zero zero = CommandCenter.getInstance().getZero();
        for (Movable enemy: CommandCenter.getInstance().getMovEnemies()) {
            Character charEnemy = (Character) enemy;

            // only both enemy and player are alive we perform this check
            if (!enemy.isProtected() && !zero.isProtected()) {

                // player enter the view cycle of enemies
                if (zero.getCenter().distance(charEnemy.getCenter()) < (zero.getRadius() + charEnemy.getViewRadius())) {
                    if (zero.getCenter().distance(charEnemy.getCenter()) < (zero.getRadius() + charEnemy.getAttackRadius())) {

                        // on the basis of entering the field of view
                        // if body circle overlap, enter attack mode
                        charEnemy.setChasing(false);
                        charEnemy.setCanAttack(true);

                        // set facing direction
                        if (zero.getCenter().x < charEnemy.getCenter().x) {
                            charEnemy.setAtLeft(true);
                        } else {
                            charEnemy.setAtLeft(false);
                        }
                        charEnemy.attack();

                    } else {

                        // otherwise start chasing
                        charEnemy.setCanAttack(false);
                        charEnemy.setChasing(true);
                        charEnemy.setNoticed(true);
                        if (CommandCenter.getInstance().getFrame() % 6 == 0) {
                            Sound.playSound(String.format("Enemy/sound_generic_enemy_run_%d.wav", runSoundIdx % 4 + 1));
                            runSoundIdx += 1;
                            if (runSoundIdx > 10000) {
                                runSoundIdx = 0;
                            }
                        }

                        // set facing direction
                        if (zero.getCenter().x < charEnemy.getCenter().x) {
                            charEnemy.setAtLeft(true);
                        } else {
                            charEnemy.setAtLeft(false);
                        }
                    }
                } else {

                    // not in view range or leave the view range,
                    // stop chasing
                    charEnemy.setChasing(false);
                }
            }
        }

    }

}
