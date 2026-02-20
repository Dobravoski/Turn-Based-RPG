package RPG;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Character {

    private final String name;
    private double hpMax;
    private double hp;
    private int manaMax;
    private int mana;
    private int level;
    private final int difficulty;  // -1, 0, 1 -> hard, normal, easy
    private int shild;
    private int bleedingRoundsCount = 0;
    private int defendRoundsCount = 0;
    private int stunRoundsCount = 0;
    private final String[] potions = {"Poção de cura simples - 2d4", "Poção de cura normal - 2d10", "Poção de cura complexa - 2d20 (Chance de ficar atordoado por 5 rodadas)",
    "Poção de mana simples - 2d6", "Poção de mana normal - 2d10", "Poção de mana complexa - 2d20 + 5 (Chance de ficar atordoado por 5 rodadas)",
    "Sopa suspeita (efeito aleatório)", "Sopa extremamente suspeita (2 efeitos aleatórios extremamente fortes)"};
    protected int[] quantityOfPotions;

    public Character(String name, double hp, int mana, int level, int difficulty) {
        this.name = name;
        this.hpMax = hp;
        this.hp = hp;
        this.manaMax = mana;
        this.mana = mana;
        this.level = level;
        this.difficulty = difficulty;
    }

    public int random(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }

    public String getName() {
        return name;
    }

    public double getHp() {
        return hp;
    }

    public double getHpMax() {return hpMax;}

    public void levelUpHP(double newHp) {
        this.hpMax = newHp;
        if (this.hp + (newHp/2) >= this.hpMax) {
            this.hp = newHp;
        } else {
            this.hp += newHp / 2;
        }
    }

    public void levelUpMana(int newMana) {
        this.manaMax = newMana;
        if (this.mana + (newMana/2) >= this.manaMax) {
            this.mana = newMana;
        } else {
            this.mana += newMana / 2;
        }
    }

    public int getMana() {
        return mana;
    }

    public int getManaMax() {return manaMax;}

    public void damage(double damage) {
        if (!dodge()) {
            if (hp - ((damage - shild) * (1 - defendRoundsCount *0.6)) <= 0) {
                hp = 0;
                System.out.println("\n" + this.name + " está morto");
            } else {
                if (random(1, 100) <= 3) { // 3% chance
                    bleeding();
                }
                hp -= (damage - shild) * (1 - defendRoundsCount *0.6);
                if (bleedingRoundsCount > 0) {
                    hp--;
                    bleedingRoundsCount--;
                    System.out.println("-1HP (Sangramento) em " + this.name);
                }
                if (defendRoundsCount > 0) {
                    defendRoundsCount--;}
            }
        }
    }

    public void heal(double heal) {
        if (hp + heal > hpMax) {
            hp = hpMax;
        } else {
            hp += heal;
        }
    }

    public void spendMana(int spent) {
        if (mana - spent < 0) {
            mana = 0;
        } else {
            mana -= spent;
        }
    }

    public void increaseMana(int increase) {
        if (mana + increase > manaMax) {
            mana = manaMax;
        } else {
            mana += increase;
        }
    }

    public int getLevel() {
        return level;
    }

    public void increaseLevel() {
        this.level++;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void bleeding() {
        if (bleedingRoundsCount == 0 || bleedingRoundsCount > 5) {
            System.out.println("\nSangramento aplicado em " + this.name);
            bleedingRoundsCount = 3;
        }
    }

    public int getBleedingRoundsCount() {return bleedingRoundsCount;}

    public void defend(int quantity) {
        if (defendRoundsCount == 0 || defendRoundsCount > quantity) {
            defendRoundsCount = quantity;
            System.out.println(this.name + " usou defender");
        }
    }

    public void stun(int quantityOfRounds, int chance) {
        if (random(1, 100) <= chance) {
            if (stunRoundsCount == 0 || stunRoundsCount > quantityOfRounds) {
                stunRoundsCount = quantityOfRounds + 1; // +1 to correct the quantity of rounds
                System.out.println(this.name + " está atordoado por " + (quantityOfRounds) +  " rodadas");
            }
        } else if (stunRoundsCount == 0) {
            System.out.println(getName() + " conseguiu evitar de ficar atordoado");
        }
        if (stunRoundsCount > 0) {
            System.out.println();
            stunRoundsCount--;
        }
    }

    public void potionsEffect(int potion) {
        int healingHP, healingMP;
        switch (potion) {
            case 0:
                healingHP = random(1,4) + random(1, 4) + 2;
                System.out.println(this.name + " curou  " + healingHP + "HP com poção de cura simples");
                heal(healingHP);
                break;
            case 1:
                healingHP = random(1,10) + random(1, 10) + 5;
                System.out.println(this.name + " curou  " + healingHP + "HP com poção de cura normal");
                heal(healingHP);
                break;
            case 2:
                healingHP = random(1,20) + random(1, 20) + 10;
                heal(healingHP);
                System.out.println(this.name + " curou  " + healingHP + "HP com poção de cura complexa");
                stun(2, 75);
                break;
            case 3:
                healingMP = random(1,6) + random(1, 6);
                increaseMana(healingMP);
                System.out.println(this.name + " ganhou  " + healingMP + " de mana com poção de mana simples");
                break;
            case 4:
                healingMP = random(1,10) + random(1, 10);
                increaseMana(healingMP);
                System.out.println(this.name + " ganhou  " + healingMP + " de mana com poção de mana normal");
                break;
            case 5:
                healingMP = random(1,20) + random(1, 20) + 5;
                increaseMana(healingMP);
                stun(2, 75);
                System.out.println(this.name + " ganhou  " + healingMP + " de mana com poção de mana complexa");
                break;
            case 6:
                System.out.println(this.name + " usou sopa suspeita");
                randomEffectOfStrangeSoup();
                break;
            case 7:
                System.out.println(this.name + " usou sopa extremamente suspeita");
                for (int i = 0; i < 2; i++) {
                    randomEffectOfStrangeSoup();
                }
                break;
        }
    }

    public void randomEffectOfStrangeSoup() {
        int healingHP, healingMP;
        int randomEffect = random(1, 6);
        switch (randomEffect) {
            case 1:
                stun(2, 100);
                break;
            case 2:
                defend(2);
                break;
            case 3:
                healingHP = random(1,20) + random(1, 20);
                System.out.println(this.name + " curou  " + healingHP + "HP com sopa suspeita");
                heal(healingHP);
                break;
            case 4:
                int damage = random(1, 20);
                System.out.println(this.name + " recebeu -" + damage + "HP com sopa suspeita");
                damage(damage);
                break;
            case 5:
                healingMP = random(1,20) + random(1, 20);
                increaseMana(healingMP);
                System.out.println(this.name + " ganhou  " + healingMP + " de mana com sopa suspeita");
                break;
            case 6:
                healingMP = random(1,20) + random(1, 20);
                spendMana(healingMP);
                System.out.println(this.name + " perdeu  -" + healingMP + " de mana com sopa suspeita");
                break;
        }
    }

    public void magic(int choose, Character character) {

        int dice1;
        int dice2;

        switch (choose) {
            case 0:
                System.out.println(this.getName() + " usou bola de fogo");
                dice1 = random(1, 12);
                dice2 = random(1, 12);
                System.out.println(this.getName() + " tirou " + dice1 + " e " + dice2 + " no dado de dano");
                spendMana(8);
                character.damage(dice1 + dice2);
                break;
            case 1:
                System.out.println(this.getName() + " usou Cura Fraca");
                dice1 = random(1, 8);
                System.out.println(this.getName() + " curou " + dice1 + " de HP");
                heal(dice1);
                spendMana(3);
                break;
            case 2:
                System.out.println(this.getName() + " usou Cura Forte");
                dice1 = random(1, 10);
                dice2 = random(1, 10);
                System.out.println(this.getName() + " tirou " +  dice1 + " e " + dice2);
                System.out.println(this.getName() + " curou " + (dice1 + dice2) + " de HP");
                heal(dice1+dice2);
                spendMana(6);
                break;
            case 3:
                System.out.println(this.getName() + " usou atordoar");
                spendMana(5);
                character.stun(2, 100);
                break;
            case 4:
                System.out.println(this.getName() + " usou Bêbado Equilibrista");
                spendMana(8);
                damage(2);
                character.increaseDodgeModifier(-5);
                System.out.print("\nAgora " + character.getName() + " tem somente " + character.getDodgeChance() + "% de esquiva\n");
                break;
            case 5:
                System.out.println(this.getName() + " usou Sangramento");
                spendMana(5);
                damage(2);
                character.bleeding();
                break;
            default:
                System.out.println("Something went wrong!");
                break;
        }
    }

    public String[] getPotions() {return potions;}

    public int getStunRoundsCount() {return stunRoundsCount;}

    public abstract boolean dodge();
    public abstract void increaseDodgeModifier(int dodgeModifier);
    public abstract int getDodgeChance();
}