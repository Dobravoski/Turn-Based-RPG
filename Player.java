package RPG;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player extends Character{

    private static final Scanner sc = new Scanner(System.in);

    private final int intelligence;
    private final int constitution;  // intelligence, constitution and strength range from 0 to 10
    private final int strength;
    private final int agility;
    private int dodgeModifier = 0;
    private int dodgeChance;
    private double shild = 0;
    private int xp = 0;
    private final String[] spells = {"(8MP) Bola de fogo - 2d12", "(3MP) Cura fraca - 1d8", "(6MP) Cura forte 2d10", "(5MP) Atordoar (25% de chance do inimigo perder 3 rodadas)",
    "(8MP) Bêbado equilibrista (-2HP, inimigo -5% de reflexo)", "(5MP) Sangramento (-2HP, inimigo -1HP por rodada, por 3 rodadas)"};
    private final Integer[] spellsAvailable = {1, 2, 3, 4, 5, 6};
    private int[] quantityOfPotions = {5, 3, 2, 5, 3, 1, 2, 2};

    public Player(String name, int constitution, int intelligence, int strength, int agility, int level, int difficulty) {
        double hpPlayer = (30 + constitution * 8 + level * 12) * (1 + 0.06 * difficulty);
        int manaPlayer = (int)((15 + intelligence * 6 + level * 8) * (1 + 0.05 * difficulty));
        super(name, hpPlayer, manaPlayer, level, difficulty);
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.constitution = constitution;
        this.dodgeChance =  10 + (agility * 30 / 100) + (level * 20 / 100) + difficulty + dodgeModifier;
    }

    private void levelUp() {
        increaseLevel();
        this.dodgeChance = (int) (0.10 + (agility * 0.02) + (getLevel() * 0.03) + (getDifficulty() * -0.01) + dodgeModifier);
        levelUpHP((30 + constitution * 8 + getLevel() * 12) * (1 + 0.06 * getDifficulty()));
        levelUpMana((int)((15 + intelligence * 6 + getLevel() * 8) * (1 + 0.05 * getDifficulty())));
    }

    public void increaseDodgeModifier(int increase) {
        this.dodgeModifier += increase;
        this.dodgeChance = (int) (0.10 + (agility * 0.02) + (getLevel() * 0.03) + (getDifficulty() * -0.01) + dodgeModifier);
    }

    public int getDodgeChance() {return dodgeChance;}

    @Override
    public boolean dodge() {
        int test = random(1, 100);
        if (test <= dodgeChance) {
            System.out.println(dodgeChance + " " + test);
            System.out.println("\n" + getName() + " se esquivou do ataque inimigo ou evitou algum tipo de dano");
            return true;
        } else {
            return false;
        }
    }

    public void increaseXP(int increase) {
        this.xp += increase;
        if (xp >= (20 * (getLevel() + 1) * (getLevel() + 1))) {
            System.out.println(getName() + " passou de nível");
            levelUp();
        }
    }

    public void increaseShild(int increase) {
        this.shild += increase;
    }

    public double getShild() {return shild;}

    public void attack(Enemy enemy) {
        int damage = random(1, 8) + this.strength;
        System.out.println("Você deu " + damage + " de dano em " + enemy.getName());
        enemy.damage(damage);
    }

    public int attack(int damage, Enemy enemy) {
        System.out.println("Você deu " + damage + " de dano em " + enemy.getName());
        return damage;
    }

    public void useMagic(List<Enemy> enemies) {
        System.out.println("Magias disponíveis:");
        for (int spellIndex : spellsAvailable) {
            System.out.println("(" + (spellIndex) + ") " + this.spells[spellIndex -1]);
        }
        int choose;
        do {
            choose = Integer.parseInt(sc.nextLine()) - 1;
            if ((choose == 0 && getMana() < 8) || (choose == 1 && getMana() < 3) ||  (choose == 2 && getMana() < 6) ||
                    (choose == 3 && getMana() < 5) || (choose == 4 && getMana() < 8) || (choose == 5 && getMana() < 5)) {
                System.out.println("Mana insuficiente!");
                continue;
            }

            if (!List.of(spellsAvailable).contains(choose+1)) {
                System.out.println("Escolha uma magia existente!");
            } else {
                break;
            }
        } while (true);
        magic(choose, whichEnemy(enemies) );
    }

    public Enemy whichEnemy(List<Enemy> enemies) {
        int  whichEnemy = 0;
        if (enemies.size() > 1) {
            System.out.println("Selecione um inimigo: ");
            do {
                whichEnemy = Integer.parseInt(sc.nextLine()) - 1;
                if (whichEnemy < 0 || whichEnemy > enemies.size() - 1) {
                    System.out.println("\nOpção inválida, tente novamente.");
                }
            } while (whichEnemy < 0 || whichEnemy > enemies.size() - 1);
        }
        return enemies.get(whichEnemy);
    }

    public void useItens() {
        for (int i = 0; i < getPotions().length; i++) {
            System.out.println("(" + (i+1) + ") " + getPotions()[i] + " (" + quantityOfPotions[i] + " unidades)");
        }
        int choose;
         outer: do {
            choose = Integer.parseInt(sc.nextLine()) - 1;
            if (choose < 0 || choose > 7) {
                System.out.println("Opção inválida, por favor digite novamente");
                continue;
            }
            if (quantityOfPotions[choose] == 0) {
                System.out.println("Você tem 0 poções desse tipo");
            }
            if (quantityOfPotions[choose] > 0) {
                break outer;
            }
        } while (true);
        potionsEffect(choose);
        quantityOfPotions[choose]--;
    }

    public void playerTurn(List<Enemy> enemies) {

        int choose;

        if (getBleedingRoundsCount() > 0) {
            bleeding();
        }

        System.out.println("\n" + this);

        System.out.println("\n1 - Atacar  /  2 - Usar Magia  /  3 - Defender  /  4 - Intimidar  /  5 - Usar Item  /  6 - Clamar por piedade\n");

        for (int i = 0; i < enemies.size(); i++) {
            System.out.println("(" + (i + 1) + ") " + enemies.get(i).toString());
        }

        do {
            choose = Integer.parseInt(sc.nextLine());
            Enemy whichEnemy;

            switch (choose) {
                case 1:
                    whichEnemy = whichEnemy(enemies);
                    attack(whichEnemy);
                    break;
                case 2:
                    useMagic(enemies);
                    break;
                case 3:
                    defend(1);
                    break;
                case 4:
                    whichEnemy = whichEnemy(enemies);
                    System.out.println("Você usou intimidar em " + whichEnemy.getName());
                    whichEnemy.stun(2, 25);
                    break;
                case 5:
                    useItens();
                    break;
                case 6:
                    System.out.println("Sifufu, piedade só se pede na cama. Perdeu um round otário(a)");
                    break;
                default:
                    System.out.println("Escolha inválida, por favor escolha um número entre 1 e 6");
                    break;
            }
        } while (choose < 1 || choose > 6);
    }

    public static List<Player> playerCreation() {
        System.out.println("\nBem vindo ao RPG de Turno do Felipe");
        System.out.println("\nVocê fará seus personagem agora");
        System.out.print("\nNome: ");
        String name = sc.nextLine();
        System.out.println("\nVocê tem 15 pontos para distribuir entre 3 atributos: constituição, agilidade e inteligência");
        System.out.println("Cada atributo deve receber de 0 a 10 pontos");

        int constitution, intelligence, agility;
        do {
            System.out.print("Constituição: ");
            constitution = Integer.parseInt(sc.nextLine());
            System.out.print("Inteligência: ");
            intelligence = Integer.parseInt(sc.nextLine());
            System.out.print("Agilidade: ");
            agility = Integer.parseInt(sc.nextLine());

            if (constitution > 10 || intelligence > 10 || agility > 10) {
                System.out.println("[ERRO]! Algum atributo está com mais de 10 pontos");
                continue;
            }

            if (constitution < 0 || intelligence < 0 || agility < 0) {
                System.out.println("[ERRO]! Algum atributo está com menos de 10 pontos");
                continue;
            }

            if (constitution + intelligence + agility != 15) {
                System.out.println("[ERRO]! Você não usou todos os seus pontos ou usou mais de 15 pontos");
            } else {
                break;
            }
        } while (true);

        System.out.println("\nAgora definiremos sua armadura");
        System.out.println("Escolha um item:");
        System.out.println("(1) Armadura foda (+5 de Defesa/Absorção de dano)");
        System.out.println("(2) Bota de Hermes (+10% de esquiva passiva)");
        System.out.println("(3) Capa de Mago (Aumenta seu reservatório de mana)");
        System.out.println("(4) Manopola mágica (+3 de dano passivo em inimigos)");
        int choice = Integer.parseInt(sc.nextLine());

        while (choice < 1 || choice > 4) {
            System.out.println("Escolha inválida, escolha um item entre 1 e 4");
            choice = Integer.parseInt(sc.nextLine());
        }

        if (choice == 3) {
            if (intelligence + 2 > 10) {
                intelligence = 10;
            } else {
                intelligence += 2;
            }
        }

        int strength = 0;
        if (choice == 4) {
            strength = 3;
        }

        System.out.println("\nQual dificuldade você quer jogar?");
        System.out.println(" 1 - Fácil\n 2 - Médio\n 3 - Difícil");
        int choice2 = Integer.parseInt(sc.nextLine());
        while (choice2 < 1 || choice2 > 3) {
            System.out.println("Número inválido, tente novamente");
            choice2 = Integer.parseInt(sc.nextLine());
        }

        int difficulty = 2; // arbitrário
        switch (choice2) {
            case 1:
                difficulty = 1;
                break;
            case 2:
                difficulty = 0;
                break;
            case 3:
                difficulty = -1;
                break;
        }

        Player player = new Player(name, constitution, intelligence, strength, agility, 1, difficulty);

        if (choice == 1) {
            player.increaseShild(5);
        }
        if (choice == 2) {
            player.increaseDodgeModifier(10);
        }

        return new ArrayList<>(List.of(player));
    }

    @Override
    public String toString() {
        return "Nome: %s  /  HP: %.2f/%.2f  /  Mana: %d/%d  /  Escudo: %.0f".formatted(getName(), getHp(), getHpMax(), getMana(),  getManaMax(), getShild());
    }
}