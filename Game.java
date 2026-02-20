package RPG;

import java.util.*;

public class Game {
    static void main() {
        story();
    }

    private static final Scanner sc = new Scanner(System.in);

    private static void battle(List<Player> players, List<Enemy> enemies) {
        List<Character> initiative = new ArrayList<>();
        initiative.addAll(enemies);
        initiative.addAll(players);
        Collections.shuffle(initiative);
        System.out.println("\nORDEM DE INICIATIVA:");
        for (int i = 0; i < initiative.size(); i++) {
            System.out.println((i + 1) + " - " + initiative.get(i).getName());
        }

        outer: do {
            for (Character character : initiative) {
                if (character.getHp() == 0) {
                    if (character instanceof Enemy && enemies.contains(character)) {
                        enemies.remove(character);
                    } else if(character instanceof Player && players.contains(character)) {
                        players.remove(character);
                    }
                    if (enemies.isEmpty() || players.isEmpty()) {
                        break outer;
                    }
                    continue;
                }

                if (character.getStunRoundsCount() > 0) {
                    character.stun(1, 0);
                } else {
                    if (character instanceof Player) {
                        System.out.println("\n Vez de " +  character.getName());
                        ((Player) character).playerTurn(enemies);
                    } else {
                        System.out.println("\n Vez de " +  character.getName());
                        ((Enemy) character).enemyTurn(players);
                    }
                }
            }
        } while (true);

        System.out.println("\nA batalha acabou");

        if (players.isEmpty()) {
            System.out.println("GAME OVER!");
            System.exit(0);
        }

        for (Player player : players) {
            player.increaseXP(116);
        }
    }

    private static void story() {
        List<Player> players = Player.playerCreation();

        List<Enemy>[] allBattles = new ArrayList[6];
        allBattles[0] = new ArrayList<>(List.of(new Enemy("Goblin", 1, players.getFirst().getDifficulty(), 0),
                new Enemy("Goblin", 1, players.getFirst().getDifficulty(), 0)));

        allBattles[1] = new ArrayList<>(List.of(new Enemy("Bandido", 2,  players.getFirst().getDifficulty(), 2),
                new Enemy("Slime", 2, players.getFirst().getDifficulty(), 0)));

        allBattles[2] = new ArrayList<>(List.of(new Enemy("Orc mago", 3,  players.getFirst().getDifficulty(), 0),
                new Enemy("Orc mago", 3,  players.getFirst().getDifficulty(), 0)));

        allBattles[3] = new ArrayList<>(List.of(new Enemy("Lobo", 3,  players.getFirst().getDifficulty(), 0),
                new Enemy("Lobo", 3,  players.getFirst().getDifficulty(), 0),
                new Enemy("Lobo", 3,  players.getFirst().getDifficulty(), 0),
                new Enemy("Lobo", 3,  players.getFirst().getDifficulty(), 0)));

        allBattles[4] = new ArrayList<>(List.of(new Enemy("Goblin", 4,   players.getFirst().getDifficulty(), 0),
                new Enemy("Lobo", 5,  players.getFirst().getDifficulty(), 0),
                new Enemy("Orc mago", 4,  players.getFirst().getDifficulty(), 0),
                new Enemy("Slime", 3,  players.getFirst().getDifficulty(), 0)));

        allBattles[5] = new ArrayList<>(List.of(new Enemy("Golem de Pedra", 30,  players.getFirst().getDifficulty(), 5)));



        System.out.println(players.getFirst().getName() + " inicia sua jornada para derrotar um grande inimigo que está causando caos na floresta proximo a sua vila");
        System.out.println("Você encontra seu primeiro obstáculo, 2 goblins atrapalham sua viagem para floresta");
        battle(players, allBattles[0]);

        System.out.println("Após mais algumas horas de viagem, você encontra dois inimigos no meio da estrada");
        battle(players, allBattles[1]);

        System.out.println("""
                Andando por mais um tempo, você escontra uma pessoa vagando pela mesma estrada que você anda,
                ao se aproximar ele pergunte se você está indo a floresta, você diz que sim, ele pergunta
                se pode andar contigo para poder avançar com mais segurança.
                Aceitar ele na party? (1 - Sim  /  2 - Não)
                """);
        do {
            int choose = Integer.parseInt(sc.nextLine());
            if (choose == 1) {
                System.out.println("Bell Barbarias entrou na party, vocês seguem viagem até a entrada de floresta");
                players.add(new Player("Bell Barbarias", 7, 3, 3, 5, 3, players.getFirst().getDifficulty()));
                break;
            } else if (choose == 2) {
                System.out.println("Você recusa a proposta do rapaz, você segue em frente e o ultrapssa");
                break;
            } else {
                System.out.println("Digite 1 ou 2!");
            }
        } while (true);

        System.out.println("Você(s) chegam na entrada da floresta, há dois orcs protegendo a entrada, eles não parecem que querem conversar");
        System.out.println("Vocês avançam cada vez mais próximo da entrada, eles avançam em você...");
        battle(players, allBattles[2]);

        System.out.println("Anoiteceu, você precisa dormir, você faz um fogueira e prepara algumas armadilhas de som para te acordar caso for atacado");
        System.out.println("Voce adormece, você não sabe dizer quanto tempo você dormiu, você acorda com os barulhos de sua armadilha");
        System.out.println("Há 4 lobos em volta de você(s)");
        battle(players, allBattles[3]);

        System.out.println("Ao finalizar essa luta, um elfo da floresta aparece, ele pergunta se você está atrás do maldito golem, você diz que sim");
        System.out.println("Ele pergunta se pode te ajudar a matar-lo, você deixa ele entrar na party? (1 - Sim  / 2 - Não");

        do {
            int choose = Integer.parseInt(sc.nextLine());
            if (choose == 1) {
                System.out.println("Rufus Aureliades entrou na party, vocês seguem viagem até a entrada de floresta");
                players.add(new Player("Rufus Aureliades", 4, 8, 5, 8, 4, players.getFirst().getDifficulty()));
                break;
            } else if (choose == 2) {
                System.out.println("Você recusa a proposta do rapaz, você segue em frente e o ultrapssa");
                break;
            } else {
                System.out.println("Digite 1 ou 2!");
            }
        } while (true);

        System.out.println("você(s) encontra(m) o maldito golem");
        System.out.println("Ele te vê, mas ao invés de te atacar, ele manda seus capangas te matarem");
        battle(players, allBattles[4]);

        System.out.println("O grande Golem de Pedra, após você(s) matar(em) seus queridos capangas, avança em você(s)");
        battle(players, allBattles[5]);

        System.out.println("Parabéns você completou sua missão e virou um herói no seu vilarejo!");
    }
}