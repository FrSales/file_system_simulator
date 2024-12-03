Metodologia:

O simulador será desenvolvido em linguagem de programação Java. Ele receberá as chamadas de métodos com os devido parâmetros. Em seguida, serão implementados os métodos correspondentes aos comandos de um SO. 

O programa executará cada funcionalidade e exibirá o resultado na tela quando necessário.

Parte 1: Introdução ao Sistema de Arquivos com Journaling

Um sistema de arquivos é uma estrutura lógica usada para gerenciar, armazenar, organizar e recuperar dados em dispositivos de armazenamento, como discos rígidos, SSDs, ou unidades USB. Ele fornece um meio de comunicação entre o sistema operacional e o hardware de armazenamento, permitindo que os usuários manipulem dados de forma eficiente.

Importância dos Sistemas de Arquivos
- Organização: Armazena dados em uma estrutura hierárquica (pastas e arquivos), facilitando o acesso e a gestão da informação.
- Acesso Eficiente: Oferece métodos para localização e recuperação rápida de dados.
- Gerenciamento de Espaço: Otimiza o uso do espaço de armazenamento disponível, dividindo o dispositivo em blocos gerenciáveis.
- Confiabilidade e Integridade: Protege contra perdas de dados por meio de recursos como journaling, sistemas de recuperação e redundância.

Journaling é uma técnica usada em sistemas de arquivos para aumentar a confiabilidade e reduzir o risco de corrupção de dados em caso de falhas, como quedas de energia ou desligamentos inesperados. Ele funciona registrando as alterações que serão feitas no sistema de arquivos em um log (ou diário) antes de aplicá-las ao disco.

Funcionamento do Journaling
- Registro no Log: Antes de modificar os dados ou a estrutura do sistema de arquivos, as alterações planejadas são registradas em uma área especial do disco chamada journal.
- Execução das Alterações: Após o registro bem-sucedido, as mudanças são aplicadas ao sistema de arquivos principal.
- Confirmação: Uma vez que as alterações são concluídas, uma confirmação é gravada no journal indicando que a operação foi bem-sucedida.

Essa abordagem garante que, em caso de falha, o sistema possa verificar o journal e completar ou reverter as operações pendentes, restaurando o sistema a um estado consistente.

Vantagens do Journaling
- Redução do Tempo de Recuperação: Após falhas, o sistema precisa apenas verificar o journal em vez de escanear todo o sistema de arquivos.
- Maior Confiabilidade: Protege contra corrupção de dados em eventos inesperados.

Tipos de Journaling
Write-Ahead Logging (Journaling Clássico):
- Neste método, todas as operações são registradas no journal antes de serem aplicadas ao sistema principal.
Exemplo: Ext3 (com modos data=ordered ou data=writeback).

Log-Structured File Systems:
- O sistema de arquivos é organizado de forma semelhante ao journal, escrevendo dados e metadados sequencialmente em vez de atualizações in-place.
Exemplo: NILFS (New Implementation of a Log-Structured File System).

Metadata Journaling:
- Apenas as alterações relacionadas aos metadados (estrutura do sistema de arquivos) são registradas no journal. Os dados reais são atualizados diretamente no sistema principal.
Exemplo: Ext4.

Full Data Journaling:
- Tanto os metadados quanto os dados reais são registrados no journal. Isso aumenta a confiabilidade, mas pode ser mais lento.
Exemplo: Modo data=journal no Ext3.

Parte 2: Arquitetura do Simulador

Estrutura de Dados: A classe File foi implementada com uma estrutura simples que contém nome e conteúdo do arquivo. Já a classe Directory possui 2 maps que contém os arquivos e subdiretórios de cada diretório

Journaling: Foi implementado usando uma lista. Todos os comandos dados ao sistema são salvos com append em uma lista de strings. 

Parte 3: Implementação em Java

Classe "FileSystemSimulator": Implementa o simulador do sistema de arquivos, incluindo métodos para cada operação.

```java
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
```

Classes File e Directory: Representam arquivos e diretórios.
```java
    // Classe para representar um Diretório
    static class Directory {
        String nome;
        Map<String, Directory> subDiretorios = new HashMap<>();
        Map<String, File> arquivos = new HashMap<>();

        Directory(String nome) {
            this.nome = nome;
        }
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
```

Classe Journal: Gerencia o log de operações.
```java
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
```

