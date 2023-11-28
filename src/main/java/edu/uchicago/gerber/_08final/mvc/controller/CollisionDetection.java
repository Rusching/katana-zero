package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.*;
import edu.uchicago.gerber._08final.mvc.model.Character;

public class CollisionDetection {
    /*
    When determine collision, enemies has three circles:
    1. body circle: used to determine whether be hit by the katana; and
        when to enter the attack mode
    2. view circle: used to determine whether it can see the character and start to chase
    3. attack circle: used to determine whether it can hit the character
     */


    /**
     * check if character's attack hit some enemies.
     * only valid if:
     *      1. character is attacking,
     *      2. katana's circle overlap with some enemies' body circle
     */
    public static int runSoundIdx = 1;

    public static void checkAllCollisions() {
        checkPlayerAttackResult();
        checkEnemyAttackResult();
        checkEnemyViewRange();
    }
    public static void checkPlayerAttackResult() {
        Zero zero = CommandCenter.getInstance().getZero();
        if (zero.isAttack()) {
            // only in attack action we check the collision between
            // katana and enemies
            Katana currentKatana = zero.getKatana();
            int katanaRadius = currentKatana.getRadius();
            int enemyRadius = Character.MIN_RADIUS;
            for (Movable enemy: CommandCenter.getInstance().getMovEnemies()) {
                if (currentKatana.getCenter().distance(enemy.getCenter()) < (katanaRadius + enemyRadius)) {
                    if (!enemy.isProtected()) {
                        if (currentKatana.getHitSlashDebris() == null) {
                            HitSlashDebris hitSlashDebris = new HitSlashDebris(currentKatana.getCenter(), enemy.getCenter());
                            currentKatana.setHitSlashDebris(hitSlashDebris);
                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    hitSlashDebris,
                                    GameOp.Action.ADD
                            );
                        }
                        ((Character) enemy).getHurt(currentKatana);
                        Sound.playSound(String.format("Enemy/sound_enemy_death_sword_0%d.wav", Game.R.nextInt(2) + 1));
                        Sound.playSound("Enemy/sound_enemy_death_generic.wav");
                        CommandCenter.getInstance().enemyNums -= 1;
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
                            BulletReflectionDebris bulletReflectionDebris = new BulletReflectionDebris(currentKatana.getCenter());
                            currentKatana.setBulletReflectionDebris(bulletReflectionDebris);
                            CommandCenter.getInstance().getOpsQueue().enqueue(
                                    bulletReflectionDebris,
                                    GameOp.Action.ADD
                            );
                        }
                        Sound.playSound("Bullet/slash_bullet.wav");
                        bulletObj.setReflected(true);
                        bulletObj.setYVelocity(-bulletObj.getYVelocity());
                        bulletObj.setXVelocity(-bulletObj.getXVelocity() * 3);
                    }
                }
            }
        }
    }

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
                            CommandCenter.getInstance().getOpsQueue().enqueue(bullet, GameOp.Action.REMOVE);
                            ((Character) enemy).getHurt(bulletObj);
                            Sound.playSound("Bullet/sound_enemy_death_bullet.wav");
                            Sound.playSound("Enemy/sound_enemy_death_generic.wav");
                            CommandCenter.getInstance().enemyNums -= 1;
                        }
                    }
                }
            }
        }

        // check if bullet is hit on brick
        for (Movable bullet: CommandCenter.getInstance().getMovBullets()) {
            for (Movable block: CommandCenter.getInstance().getMovFloors()) {
                if (bullet.getCenter().distance(block.getCenter()) < (bullet.getRadius() + block.getRadius())) {
                    CommandCenter.getInstance().getOpsQueue().enqueue(bullet, GameOp.Action.REMOVE);
                }
            }
        }
    }

    public static void checkEnemyViewRange() {
        Zero zero = CommandCenter.getInstance().getZero();
        for (Movable enemy: CommandCenter.getInstance().getMovEnemies()) {
            Character charEnemy = (Character) enemy;
            if (!enemy.isProtected() && !zero.isProtected()) {
                if (zero.getCenter().distance(charEnemy.getCenter()) < (zero.getRadius() + charEnemy.getViewRadius())) {
                    if (zero.getCenter().distance(charEnemy.getCenter()) < (zero.getRadius() + charEnemy.getAttackRadius())) {
                        // if body circle overlap, enter attack mode
                        charEnemy.setChasing(false);
                        charEnemy.setCanAttack(true);
                        if (zero.getCenter().x < charEnemy.getCenter().x) {
                            charEnemy.setAtLeft(true);
                        } else {
                            charEnemy.setAtLeft(false);
                        }
                        charEnemy.attack();

                    } else {
                        // chase
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
                        if (zero.getCenter().x < charEnemy.getCenter().x) {
                            charEnemy.setAtLeft(true);
                        } else {
                            charEnemy.setAtLeft(false);
                        }
                    }


                } else {
                    charEnemy.setChasing(false);
                }
            }
        }

    }

}
