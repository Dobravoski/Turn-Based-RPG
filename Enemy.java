package RPG;

import java.util.List;

public class Enemy extends Character{

    private int shild = 0;
    private int dodgeChance;
    private int[] quantityOfPotions = {2, 1, 0, 2, 0, 0, 0, 0};  // arrumar lógica para diminuir a quantidade
    private int strength;

    public Enemy(String name, int level, int difficulty, int strength) {
        int hpEnemy = (int)((20 + level * 10) * (1 - 0.06 * difficulty));
        int manaEnemy = (int)((8 + level * 5) * (1 - 0.05 * difficulty));
        this.dodgeChance = 5 + (level * 80 / 100) + difficulty;
        this.strength = strength;
        super(name, hpEnemy, manaEnemy, level, difficulty);
    }

    public void increaseDodgeModifier(int dodgeModifier) {
        if (dodgeChance + dodgeModifier >= 0 &&  dodgeChance + dodgeModifier <= 100) {
            this.dodgeChance += dodgeModifier;
        } else if(dodgeChance + dodgeModifier < 0) {
            this.dodgeChance = 0;
        } else {
            this.dodgeChance = 100;
        }
    }

    public void attack(Player player, int dX) {
        int damage = random(1, dX) + strength;
        System.out.println("\n" + getName() + " deu  " + damage + " de dano em " +  player.getName());
        player.damage(damage);
    }

    @Override
    public boolean dodge() {
        int test = random(1, 100);
        if (test <= dodgeChance) {
            System.out.println(dodgeChance + " " + test);
            System.out.println("\nO inimigo se esquivou de seu ataque");
            return true;
        } else {
            return false;
        }
    }

    public void enemyTurn(List<Player> players) {
        Player whichPlayer = players.get(random(0, players.size() - 1));
        int randomChoose = random(1,10);
        switch (getName()) {
            case "Goblin":
                if (randomChoose <= 8) {
                    attack(whichPlayer, 6);
                } else if (randomChoose == 9) {
                    defend(1);
                } else {
                    potionsEffect(0); // Simple cure
                }
                break;
            case "Orc mago":
                if (randomChoose <= 2) {
                    attack(whichPlayer, 8);
                } else if (randomChoose == 3 || randomChoose == 4) {
                    defend(1);
                } else {
                    int randomMagic;
                    do {
                        randomMagic = random(0, 5); // 0, 1, 3, 5
                    } while (randomMagic == 2 || randomMagic == 4);
                    magic(randomMagic, whichPlayer);
                }
                break;
            case "Bandido":
                if (randomChoose <= 5) {
                    attack(whichPlayer, 6);
                } else if (randomChoose == 6 || randomChoose == 7) {
                    defend(1);
                } else {
                    System.out.println(getName() + " usou magia de sangramento em " + whichPlayer.getName());
                    whichPlayer.bleeding();
                }
                break;
            case "Slime":
                if (randomChoose <= 4) {
                    attack(whichPlayer, 6);
                } else if (randomChoose <= 8) {
                    defend(1);
                } else {
                    magic(3, whichPlayer);
                }
                break;
            case "Lobo":
                if (randomChoose <= 6) {
                    attack(whichPlayer, 10);
                } else {
                    defend(1);
                }
                break;
            case "Golem de Pedra":
                if (randomChoose <= 4) {
                    attack(whichPlayer, 15);
                } else if (randomChoose == 5 || randomChoose == 6) {
                    System.out.println("Golem de pedra bate no chão e causa um mini-terremoto");
                    for (Player player : players) {
                        attack(player,15);
                    }
                } else if (randomChoose == 7 || randomChoose == 8) {
                    defend(1);
                } else {
                    whichPlayer.stun(1, 50);
                }
                break;
            default:
                System.out.println("Error, enemy not found (404)");
                break;
        }
    }

    @Override
    public int getDodgeChance() {return dodgeChance;}

    @Override
    public String toString() {return "%s - %.2f/%.2fHP ".formatted(getName(), getHp(), getHpMax());}
}