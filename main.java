import java.io.*;
import java.util.*;

// 프로세스 클래스
class Process {
    int id;
    int arrivalTime;
    int burstTime;
    int priority;
    int timeQuantum;
    int waitingTime;
    int turnaroundTime;
    int responseTime;
    int remainingTime;

    public Process(int id, int arrivalTime, int burstTime, int priority, int timeQuantum) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.timeQuantum = timeQuantum;
        this.remainingTime = burstTime;
    }
}

// FCFS 스케줄링 알고리즘
class FCFS {
    public void schedule(List<Process> processes) {
        int currentTime = 0;

        Print.printGanttChart(processes);

        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            p.responseTime = currentTime - p.arrivalTime;
            p.waitingTime = p.responseTime;
            p.turnaroundTime = p.waitingTime + p.burstTime;

            currentTime += p.burstTime;
        }

        Print.printProcessDetails(processes);
    }
}

// SJF 스케줄링 알고리즘 (비선점)
class SJF {
    public void schedule(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.burstTime));
        int currentTime = 0;

        Print.printGanttChart(processes);

        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            p.responseTime = currentTime - p.arrivalTime;
            p.waitingTime = p.responseTime;
            p.turnaroundTime = p.waitingTime + p.burstTime;

            currentTime += p.burstTime;
        }

        Print.printProcessDetails(processes);
    }
}

class Print {
    public static void printGanttChart(List<Process> processes) {
        System.out.print("Gantt Chart: ");
        System.out.println();
        int maxBurstTime = processes.stream().mapToInt(p -> p.burstTime).max().orElse(0);
        for (Process p : processes) {
            System.out.print("|");
            System.out.print("P" + p.id);
            for (int i = 0; i < p.burstTime - 2; i++) {
                System.out.print("_");
            }
        }
        System.out.print("|");
        System.out.println();
        System.out.print("0");
        int currentTime = 0;
        for (Process p : processes) {
            currentTime += p.burstTime;
            for (int i = 0; i < p.burstTime; i++) {
                System.out.print(" ");
            }
            System.out.print(+ currentTime);
        }
        System.out.println();
    }

    public static void printProcessDetails(List<Process> processes) {
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int totalResponseTime = 0;

        for (Process p : processes) {
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
            totalResponseTime += p.responseTime;
        }

        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        double avgResponseTime = (double) totalResponseTime / processes.size();

        System.out.println();
        System.out.println("각 프로세스별 대기 시간:");
        for (Process p : processes) {
            System.out.print("P" + p.id + ": " + p.waitingTime + "   ");
        }
        System.out.println();
        System.out.println("평균 대기 시간: " + avgWaitingTime + "m/s");
        System.out.println();
        System.out.println("각 프로세스별 응답 시간:");
        for (Process p : processes) {
            System.out.print("P" + p.id + ": " + p.responseTime + "  ");
        }
        System.out.println();
        System.out.println("평균 응답 시간: " + avgResponseTime + "m/s");
        System.out.println();
        System.out.println("각 프로세스별 반환 시간:");
        for (Process p : processes) {
            System.out.print("P" + p.id + ": " + p.turnaroundTime + "  ");
        }
        System.out.println();
        System.out.println("평균 반환 시간: " + avgTurnaroundTime + "m/s");
        System.out.println();
    }
}
// 메인 실행 함수
public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Process> processes = readProcessesFromFile("input.txt");

        while (true) {
            System.out.println("다음 중 사용할 스케줄링 알고리즘을 선택하세요:");
            System.out.println("1. FCFS  2. SJF  3. Priority  4. NonPriority");
            System.out.println("5. RR    6. SRT  7. HRN       8. 종료");
            System.out.print("선택 : ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("FCFS 스케줄링:");
                    new FCFS().schedule(new ArrayList<>(processes));
                    break;
                case 2:
                    System.out.println("SJF 스케줄링:");
                    new SJF().schedule(new ArrayList<>(processes));
                    break;
                case 8:
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    System.exit(0); // 프로그램 종료
                    break;
                default:
                    System.out.println("올바른 선택이 아닙니다.");
            }
        }
    }

    private static List<Process> readProcessesFromFile(String filename) {
        List<Process> processes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int processCount = Integer.parseInt(reader.readLine().trim());
            for (int i = 0; i < processCount; i++) {
                String[] tokens = reader.readLine().split(",");
                int id = Integer.parseInt(tokens[0].trim());
                int arrivalTime = Integer.parseInt(tokens[1].trim());
                int burstTime = Integer.parseInt(tokens[2].trim());
                int priority = Integer.parseInt(tokens[3].trim());
                int timeQuantum = Integer.parseInt(tokens[4].trim());
                processes.add(new Process(id, arrivalTime, burstTime, priority, timeQuantum));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processes;
    }
}
