import java.util.*;

public class FileSystemSimulator {
    private final Directory raiz;
    private Directory diretorioAtual;

    // Construtor
    public FileSystemSimulator() {
        this.raiz = new Directory("raiz");
        this.diretorioAtual = raiz;
    }

    // Classe para representar um Diretorio
    static class Directory {
        String nome;
        Map<String, Directory> subDiretorios = new HashMap<>();
        Map<String, String> arquivos = new HashMap<>();

        Directory(String nome) {
            this.nome = nome;
        }
    }

    // dir conteúdo do Diretorio atual
    public void listarConteudo() {
        System.out.println("Conteudo de " + diretorioAtual.nome + ":");
        for (String dir : diretorioAtual.subDiretorios.keySet()) {
            System.out.println("[D] " + dir);
        }
        for (String file : diretorioAtual.arquivos.keySet()) {
            System.out.println("[A] " + file);
        }
    }

    // Criar arquivo no Diretorio atual
    public void criarArquivo(String nome) {
        if (diretorioAtual.arquivos.containsKey(nome)) {
            System.out.println("Arquivo ja existe: " + nome);
        } else {
            diretorioAtual.arquivos.put(nome, "Conteudo do arquivo: " + nome);
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
            System.out.println("Arquivo excluido: " + nome);
        } else {
            System.out.println("Arquivo não encontrado: " + nome);
        }
    }

    // Criar Diretorio
    public void criarDiretorio(String nome) {
        if (diretorioAtual.subDiretorios.containsKey(nome)) {
            System.out.println("Diretorio já existe: " + nome);
        } else {
            diretorioAtual.subDiretorios.put(nome, new Directory(nome));
            System.out.println("Diretorio criado: " + nome);
        }
    }

    // Renomear Diretorio
    public void renomearDiretorio(String nomeAntigo, String nomeNovo) {
        if (diretorioAtual.subDiretorios.containsKey(nomeAntigo)) {
            Directory dir = diretorioAtual.subDiretorios.remove(nomeAntigo);
            dir.nome = nomeNovo;
            diretorioAtual.subDiretorios.put(nomeNovo, dir);
            System.out.println("Diretorio renomeado para: " + nomeNovo);
        } else {
            System.out.println("Diretorio não encontrado: " + nomeAntigo);
        }
    }

    // Excluir Diretorio
    public void excluirDiretorio(String nome) {
        if (diretorioAtual.subDiretorios.remove(nome) != null) {
            System.out.println("Diretorio excluído: " + nome);
        } else {
            System.out.println("Diretorio não encontrado: " + nome);
        }
    }

    // navegar para um subDiretorio
    public void cdDiretorio(String nome) {
        if (diretorioAtual.subDiretorios.containsKey(nome)) {
            diretorioAtual = diretorioAtual.subDiretorios.get(nome);
            System.out.println("Entrou no Diretorio: " + nome);
        } else {
            System.out.println("Diretorio não encontrado: " + nome);
        }
    }

    public void voltarDiretorio() {
        if (diretorioAtual == raiz) {
           System.out.println("Você já está no Diretorio raiz.");
        } else {
             for (Directory dir : raiz.subDiretorios.values()) {
                if (navegar(dir, diretorioAtual)) {
                    return;
                }
            }
        }
    }

    private boolean navegar(Directory atual, Directory destino) {
        for (Directory sub : atual.subDiretorios.values()) {
            if (sub == destino) {
                diretorioAtual = atual;
                System.out.println("Voltou para o Diretorio: " + atual.nome);
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
            
            do {
                System.out.println("\nDiretorio atual: " + diretorioAtual.nome);
                System.out.println("Comandos: dir (lista conteudo), mkfile (cria um arquivo), renfile (renomeia um arquivo), delfile (deleta um arquivo), "
                        + "mkdir (cria um diretorio), rendir (renomeia um diretorio), deldir (deleta um diretorio), cd (navega para um subdiretorio), back (retorna para diretorio pai), exit (fecha o sistema)");
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
                        System.out.print("Nome do Diretorio: ");
                        criarDiretorio(scanner.nextLine());
                    }
                    case "rendir" -> {
                        System.out.print("Nome do Diretorio antigo: ");
                        String antigo = scanner.nextLine();
                        System.out.print("Nome do Diretorio novo: ");
                        renomearDiretorio(antigo, scanner.nextLine());
                    }
                    case "deldir" -> {
                        System.out.print("Nome do Diretorio: ");
                        excluirDiretorio(scanner.nextLine());
                    }
                    case "cd" -> {
                        System.out.print("Nome do subDiretorio: ");
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
