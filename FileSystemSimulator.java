import java.util.*;

public class FileSystemSimulator {
    private final Diretorio raiz;
    private Diretorio diretorioAtual;

    // Construtor
    public FileSystemSimulator() {
        this.raiz = new Diretorio("raiz");
        this.diretorioAtual = raiz;
    }

    // Classe para representar um diretório
    static class Diretorio {
        String nome;
        Map<String, Diretorio> subDiretorios = new HashMap<>();
        Map<String, String> arquivos = new HashMap<>();

        Diretorio(String nome) {
            this.nome = nome;
        }
    }

    // dir conteúdo do diretório atual
    public void listarConteudo() {
        System.out.println("Conteúdo de " + diretorioAtual.nome + ":");
        for (String dir : diretorioAtual.subDiretorios.keySet()) {
            System.out.println("[D] " + dir);
        }
        for (String file : diretorioAtual.arquivos.keySet()) {
            System.out.println("[A] " + file);
        }
    }

    // Criar arquivo no diretório atual
    public void criarArquivo(String nome) {
        if (diretorioAtual.arquivos.containsKey(nome)) {
            System.out.println("Arquivo já existe: " + nome);
        } else {
            diretorioAtual.arquivos.put(nome, "Conteúdo do arquivo: " + nome);
            System.out.println("Arquivo criado: " + nome);
        }
    }

    // Renomear arquivo
    public void renomearArquivo(String nomeAntigo, String nomeNovo) {
        if (diretorioAtual.arquivos.containsKey(nomeAntigo)) {
            String conteudo = diretorioAtual.arquivos.remove(nomeAntigo);
            diretorioAtual.arquivos.put(nomeNovo, conteudo);
            System.out.println("Arquivo renomeado para: " + nomeNovo);
        } else {
            System.out.println("Arquivo não encontrado: " + nomeAntigo);
        }
    }

    // Excluir arquivo
    public void excluirArquivo(String nome) {
        if (diretorioAtual.arquivos.remove(nome) != null) {
            System.out.println("Arquivo excluído: " + nome);
        } else {
            System.out.println("Arquivo não encontrado: " + nome);
        }
    }

    // Criar diretório
    public void criarDiretorio(String nome) {
        if (diretorioAtual.subDiretorios.containsKey(nome)) {
            System.out.println("Diretório já existe: " + nome);
        } else {
            diretorioAtual.subDiretorios.put(nome, new Diretorio(nome));
            System.out.println("Diretório criado: " + nome);
        }
    }

    // Renomear diretório
    public void renomearDiretorio(String nomeAntigo, String nomeNovo) {
        if (diretorioAtual.subDiretorios.containsKey(nomeAntigo)) {
            Diretorio dir = diretorioAtual.subDiretorios.remove(nomeAntigo);
            dir.nome = nomeNovo;
            diretorioAtual.subDiretorios.put(nomeNovo, dir);
            System.out.println("Diretório renomeado para: " + nomeNovo);
        } else {
            System.out.println("Diretório não encontrado: " + nomeAntigo);
        }
    }

    // Excluir diretório
    public void excluirDiretorio(String nome) {
        if (diretorioAtual.subDiretorios.remove(nome) != null) {
            System.out.println("Diretório excluído: " + nome);
        } else {
            System.out.println("Diretório não encontrado: " + nome);
        }
    }

    // navegar para um subdiretório
    public void cdDiretorio(String nome) {
        if (diretorioAtual.subDiretorios.containsKey(nome)) {
            diretorioAtual = diretorioAtual.subDiretorios.get(nome);
            System.out.println("Entrou no diretório: " + nome);
        } else {
            System.out.println("Diretório não encontrado: " + nome);
        }
    }

    public void voltarDiretorio() {
        if (diretorioAtual == raiz) {
           System.out.println("Você já está no diretório raiz.");
        } else {
             for (Diretorio dir : raiz.subDiretorios.values()) {
                if (navegar(dir, diretorioAtual)) {
                    return;
                }
            }
        }
    }

    private boolean navegar(Diretorio atual, Diretorio destino) {
        for (Diretorio sub : atual.subDiretorios.values()) {
            if (sub == destino) {
                diretorioAtual = atual;
                System.out.println("Voltou para o diretório: " + atual.nome);
                return true;
            } else if (navegar(sub, destino)) {
                return true;
            }
        }
        return false;
    }

    // Menu para interagir com o simulador
    public void iniciar() {
        try (Scanner scanner = new Scanner(System.in)) {
            String comando;
            
            System.out.println("Bem-vindo ao simulador de sistema de arquivos!");
            
            do {
                System.out.println("\nDiretório atual: " + diretorioAtual.nome);
                System.out.println("Comandos: dir, mkfile, renfile, delfile, "
                        + "mkdir, rendir, deldir, cd, back, exit");
                System.out.print("> ");
                comando = scanner.nextLine();
                
                switch (comando) {
                    case "dir" -> listarConteudo();
                    case "mkfile" -> {
                        System.out.print("Nome do arquivo: ");
                        criarArquivo(scanner.nextLine());
                    }
                    case "renfile" -> {
                        System.out.print("Nome do arquivo antigo: ");
                        String antigo = scanner.nextLine();
                        System.out.print("Nome do arquivo novo: ");
                        renomearArquivo(antigo, scanner.nextLine());
                    }
                    case "delfile" -> {
                        System.out.print("Nome do arquivo: ");
                        excluirArquivo(scanner.nextLine());
                    }
                    case "mkdir" -> {
                        System.out.print("Nome do diretório: ");
                        criarDiretorio(scanner.nextLine());
                    }
                    case "rendir" -> {
                        System.out.print("Nome do diretório antigo: ");
                        String antigo = scanner.nextLine();
                        System.out.print("Nome do diretório novo: ");
                        renomearDiretorio(antigo, scanner.nextLine());
                    }
                    case "deldir" -> {
                        System.out.print("Nome do diretório: ");
                        excluirDiretorio(scanner.nextLine());
                    }
                    case "cd" -> {
                        System.out.print("Nome do subdiretório: ");
                        cdDiretorio(scanner.nextLine());
                    }
                    case "back" -> voltarDiretorio();
                    case "exit" -> System.out.println("Encerrando o simulador...");
                    default -> System.out.println("Comando inválido.");
                }
            } while (!comando.equals("exit"));
        }
    }

    public static void main(String[] args) {
        FileSystemSimulator simulador = new FileSystemSimulator();
        simulador.iniciar();
    }
}
