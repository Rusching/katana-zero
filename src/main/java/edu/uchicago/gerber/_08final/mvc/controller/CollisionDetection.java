package edu.uchicago.gerber._08final.mvc.controller;

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
    public static void checkCharacterAttackResult() {


    }

    public static void checkEnemyAttackResult() {


    }

    public static void checkEnemyViewRange() {

    }

    public static void checkEnemyAttack() {


    }
}
