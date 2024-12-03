import java.util.*;

public class FileSystemSimulator {
    private final Directory raiz;
    private Directory diretorioAtual;
    private final Journal journal;

    // Construtor
    public FileSystemSimulator() {
        this.raiz = new Directory("raiz");
        this.diretorioAtual = raiz;
        this.journal = new Journal();
    }

    // Classe para representar um Arquivo
    static class File {
        String nome;
        String conteudo;

        File(String nome) {
            this.nome = nome;
            this.conteudo = "Conteúdo do arquivo: " + nome;
        }
    }

    // Classe para representar um Diretório
    static class Directory {
        String nome;
        Map<String, Directory> subDiretorios = new HashMap<>();
        Map<String, File> arquivos = new HashMap<>();

        Directory(String nome) {
            this.nome = nome;
        }
    }

    // Classe para gerenciar o log de operações
    static class Journal {
        private final List<String> logs = new ArrayList<>();

        void registrarOperacao(String operacao) {
            logs.add(operacao);
        }

        void exibirLogs() {
            System.out.println("Log de operações:");
            for (String log : logs) {
                System.out.println(log);
            }
        }
    }

    // Listar conteúdo do Diretório atual
    public void listarConteudo() {
        System.out.println("Conteúdo de " + diretorioAtual.nome + ":");
        for (String dir : diretorioAtual.subDiretorios.keySet()) {
            System.out.println("[D] " + dir);
        }
        for (String file : diretorioAtual.arquivos.keySet()) {
            System.out.println("[A] " + file);
        }
        journal.registrarOperacao("Listou o conteúdo do diretório: " + diretorioAtual.nome);
    }

    // Criar arquivo no Diretório atual
    public void criarArquivo(String nome) {
        if (diretorioAtual.arquivos.containsKey(nome)) {
            System.out.println("Arquivo já existe: " + nome);
        } else {
            diretorioAtual.arquivos.put(nome, new File(nome));
            System.out.println("Arquivo criado: " + nome);
            journal.registrarOperacao("Arquivo criado: " + nome);
        }
    }

    // Renomear arquivo
    public void renomearArquivo(String nomeAntigo, String nomeNovo) {
        if (diretorioAtual.arquivos.containsKey(nomeAntigo)) {
            File arquivo = diretorioAtual.arquivos.remove(nomeAntigo);
            arquivo.nome = nomeNovo;
            diretorioAtual.arquivos.put(nomeNovo, arquivo);
            System.out.println("Arquivo renomeado para: " + nomeNovo);
            journal.registrarOperacao("Arquivo renomeado de " + nomeAntigo + " para " + nomeNovo);
        } else {
            System.out.println("Arquivo não encontrado: " + nomeAntigo);
        }
    }

    // Excluir arquivo
    public void excluirArquivo(String nome) {
        if (diretorioAtual.arquivos.remove(nome) != null) {
            System.out.println("Arquivo excluído: " + nome);
            journal.registrarOperacao("Arquivo excluído: " + nome);
        } else {
            System.out.println("Arquivo não encontrado: " + nome);
        }
    }

    // Criar Diretório
    public void criarDiretorio(String nome) {
        if (diretorioAtual.subDiretorios.containsKey(nome)) {
            System.out.println("Diretório já existe: " + nome);
        } else {
            diretorioAtual.subDiretorios.put(nome, new Directory(nome));
            System.out.println("Diretório criado: " + nome);
            journal.registrarOperacao("Diretório criado: " + nome);
        }
    }

    // Renomear Diretório
    public void renomearDiretorio(String nomeAntigo, String nomeNovo) {
        if (diretorioAtual.subDiretorios.containsKey(nomeAntigo)) {
            Directory dir = diretorioAtual.subDiretorios.remove(nomeAntigo);
            dir.nome = nomeNovo;
            diretorioAtual.subDiretorios.put(nomeNovo, dir);
            System.out.println("Diretório renomeado para: " + nomeNovo);
            journal.registrarOperacao("Diretório renomeado de " + nomeAntigo + " para " + nomeNovo);
        } else {
            System.out.println("Diretório não encontrado: " + nomeAntigo);
        }
    }

    // Excluir Diretório
    public void excluirDiretorio(String nome) {
        if (diretorioAtual.subDiretorios.remove(nome) != null) {
            System.out.println("Diretório excluído: " + nome);
            journal.registrarOperacao("Diretório excluído: " + nome);
        } else {
            System.out.println("Diretório não encontrado: " + nome);
        }
    }

    // Navegar para um subdiretório
    public void cdDiretorio(String nome) {
        if (diretorioAtual.subDiretorios.containsKey(nome)) {
            diretorioAtual = diretorioAtual.subDiretorios.get(nome);
            System.out.println("Entrou no Diretório: " + nome);
            journal.registrarOperacao("Navegou para o diretório: " + nome);
        } else {
            System.out.println("Diretório não encontrado: " + nome);
        }
    }

    public void voltarDiretorio() {
        if (diretorioAtual == raiz) {
            System.out.println("Você já está no Diretório raiz.");
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
                System.out.println("Voltou para o Diretório: " + atual.nome);
                journal.registrarOperacao("Voltou para o diretório: " + atual.nome);
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
                System.out.println("\nDiretório atual: " + diretorioAtual.nome);
                System.out.println("Comandos: dir (lista conteúdo), mkfile (cria um arquivo), renfile (renomeia um arquivo), delfile (deleta um arquivo), "
                        + "mkdir (cria um diretório), rendir (renomeia um diretório), deldir (deleta um diretório), cd (navega para um subdiretório), back (retorna para diretório pai), "
                        + "log (exibe o log de operações), exit (fecha o sistema)");
                System.out.print("> ");
                comando = scanner.nextLine().trim();

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
                        System.out.print("Nome do Diretório: ");
                        criarDiretorio(scanner.nextLine());
                    }
                    case "rendir" -> {
                        System.out.print("Nome do Diretório antigo: ");
                        String antigo = scanner.nextLine();
                        System.out.print("Nome do Diretório novo: ");
                        renomearDiretorio(antigo, scanner.nextLine());
                    }
                    case "deldir" -> {
                        System.out.print("Nome do Diretório: ");
                        excluirDiretorio(scanner.nextLine());
                    }
                    case "cd" -> {
                        System.out.print("Nome do subdiretório: ");
                        cdDiretorio(scanner.nextLine());
                    }
                    case "back" -> voltarDiretorio();
                    case "log" -> journal.exibirLogs();
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
