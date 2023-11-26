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

    public static void checkAllCollisions() {
        checkPlayerAttackResult();
        checkEnemyAttackResult();
        checkEnemyViewRange();
        checkEnemyAttack();
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
                        if (enemy instanceof Grunt) {

                            Sound.playSound(String.format("Enemy/sound_enemy_death_sword_0%d.wav", Game.R.nextInt(2)));
                            System.out.println("Grunt hurt to ground");
                            System.out.println("Katana center: " + currentKatana.getCenter().x + " " + currentKatana.getCenter().y);
                            System.out.println("Grunt center: " + enemy.getCenter().x + " " + enemy.getCenter().y);
                            Grunt gruntEnemy = (Grunt) enemy;
                            gruntEnemy.getHurt(currentKatana);
                        }
                    }
                }
            }
        }
    }

    public static void checkEnemyAttackResult() {


    }

    public static void checkEnemyViewRange() {
        Zero zero = CommandCenter.getInstance().getZero();
        for (Movable enemy: CommandCenter.getInstance().getMovEnemies()) {
            Character charEnemy = (Character) enemy;
            if (!enemy.isProtected()) {
                if (zero.getCenter().distance(charEnemy.getCenter()) < (zero.getRadius() + charEnemy.viewRadius)) {
                    if (zero.getCenter().distance(charEnemy.getCenter()) < (zero.getRadius() + charEnemy.getAttackRadius())) {
                        // if body circle overlap, enter attack mode
                        charEnemy.setChasing(false);
                        charEnemy.setCanAttack(true);
                        charEnemy.attack();

                    } else {
                        // chase

                        charEnemy.setCanAttack(false);
                        charEnemy.setChasing(true);
                        charEnemy.setNoticed(true);
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

    public static void checkEnemyAttack() {


    }
}
