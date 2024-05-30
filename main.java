import java.io.*;
import java.util.*;

// 프로세스 클래스
class Process {
    int id;                 // 프로세스 ID
    int arrivalTime;        // 도착 시간
    int burstTime;          // 실행 시간
    int priority;           // 우선 순위
    int waitingTime;        // 대기 시간
    int turnaroundTime;     // 반환 시간
    int responseTime;       // 응답 시간
    int remainingTime;      // 남은 실행 시간

    public Process(int id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime; // 남은 실행 시간 초기화
    }
}

// FCFS 스케줄링 알고리즘 클래스
class FCFS {
    public void schedule(List<Process> processes) {
        int currentTime = 0; // 현재 시간 초기화

        for (Process process : processes) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime; // 도착 시간이 현재 시간보다 크면 현재 시간 갱신
            }
            process.responseTime = currentTime - process.arrivalTime; // 응답 시간 계산
            process.waitingTime = process.responseTime; // 대기 시간 = 응답 시간
            process.turnaroundTime = process.waitingTime + process.burstTime; // 반환 시간 = 대기 시간 + 실행 시간

            currentTime += process.burstTime; // 현재 시간 갱신
        }

        Print.printGanttChart(processes); // 간트 차트 출력
        Print.printProcessDetails(processes); // 프로세스 상세 정보 출력
    }
}

// SJF(Shortest Job First) 스케줄링 알고리즘 (비선점)
class SJF {
    public void schedule(List<Process> processes) {
        processes.sort(Comparator.comparingInt(proc -> proc.burstTime)); // 실행 시간 기준으로 정렬
        int currentTime = 0; // 현재 시간 초기화

        for (Process process : processes) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime; // 도착 시간이 현재 시간보다 크면 현재 시간 갱신
            }
            process.responseTime = currentTime - process.arrivalTime; // 응답 시간 계산
            process.waitingTime = process.responseTime; // 대기 시간 = 응답 시간
            process.turnaroundTime = process.waitingTime + process.burstTime; // 반환 시간 = 대기 시간 + 실행 시간

            currentTime += process.burstTime; // 현재 시간 갱신
        }

        Print.printGanttChart(processes); // 간트 차트 출력
        Print.printProcessDetails(processes); // 프로세스 상세 정보 출력
    }
}

// 비선점 우선순위 스케줄링 알고리즘 클래스
class NonPreemptivePriority {
    public void schedule(List<Process> processes) {
        processes.sort(Comparator.comparingInt(proc -> proc.priority)); // 우선 순위 기준으로 정렬
        int currentTime = 0; // 현재 시간 초기화

        for (Process process : processes) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime; // 도착 시간이 현재 시간보다 크면 현재 시간 갱신
            }
            process.responseTime = currentTime - process.arrivalTime; // 응답 시간 계산
            process.waitingTime = process.responseTime; // 대기 시간 = 응답 시간
            process.turnaroundTime = process.waitingTime + process.burstTime; // 반환 시간 = 대기 시간 + 실행 시간

            currentTime += process.burstTime; // 현재 시간 갱신
        }

        Print.printGanttChart(processes); // 간트 차트 출력
        Print.printProcessDetails(processes); // 프로세스 상세 정보 출력
    }
}

// 선점 우선순위 스케줄링 알고리즘 클래스
class PreemptivePriority {
    public void schedule(List<Process> processes) {
        List<Process> completedProcesses = new ArrayList<>(); // 완료된 프로세스 리스트
        processes.sort(Comparator.comparingInt(proc -> proc.arrivalTime)); // 도착 시간 기준으로 정렬
        PriorityQueue<Process> queue = new PriorityQueue<>(Comparator.comparingInt(proc -> proc.priority)); // 우선순위 큐
        int currentTime = 0; // 현재 시간 초기화

        while (!processes.isEmpty() || !queue.isEmpty()) { // 프로세스 또는 큐가 비어있지 않을 때
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                queue.add(processes.remove(0)); // 도착한 프로세스를 큐에 추가
            }

            if (queue.isEmpty()) {
                currentTime = processes.get(0).arrivalTime; // 큐가 비어있으면 현재 시간을 다음 도착 시간으로 갱신
                continue;
            }

            Process process = queue.poll();
            if (process.remainingTime == process.burstTime) {
                process.responseTime = currentTime - process.arrivalTime; // 첫 실행 시 응답 시간 계산
            }
            currentTime++;
            process.remainingTime--;

            if (process.remainingTime == 0) {
                process.turnaroundTime = currentTime - process.arrivalTime; // 반환 시간 계산
                process.waitingTime = process.turnaroundTime - process.burstTime; // 대기 시간 계산
                completedProcesses.add(process); // 완료된 프로세스 리스트에 추가
            } else {
                queue.add(process); // 남은 실행 시간이 있으면 큐에 다시 추가
            }
        }

        Print.printGanttChart(completedProcesses); // 간트 차트 출력
        Print.printProcessDetails(completedProcesses); // 프로세스 상세 정보 출력
    }
}

// 라운드 로빈(RR) 스케줄링 알고리즘 클래스
class RR {
    int quantum; // 시간 할당량

    public RR(int quantum) {
        this.quantum = quantum; // 시간 할당량 초기화
    }

    public void schedule(List<Process> processes) {
        List<Process> completedProcesses = new ArrayList<>(); // 완료된 프로세스 리스트
        processes.sort(Comparator.comparingInt(proc -> proc.arrivalTime)); // 도착 시간 기준으로 정렬
        Queue<Process> queue = new LinkedList<>(); // 큐
        int currentTime = 0; // 현재 시간 초기화

        while (!processes.isEmpty() || !queue.isEmpty()) { // 프로세스 또는 큐가 비어있지 않을 때
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                queue.add(processes.remove(0)); // 도착한 프로세스를 큐에 추가
            }

            if (queue.isEmpty()) {
                currentTime = processes.get(0).arrivalTime; // 큐가 비어있으면 현재 시간을 다음 도착 시간으로 갱신
                continue;
            }

            Process process = queue.poll();
            if (process.remainingTime == process.burstTime) {
                process.responseTime = currentTime - process.arrivalTime; // 첫 실행 시 응답 시간 계산
            }
            int execTime = Math.min(quantum, process.remainingTime); // 실행 시간 결정
            currentTime += execTime;
            process.remainingTime -= execTime;

            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                queue.add(processes.remove(0)); // 도착한 프로세스를 큐에 추가
            }

            if (process.remainingTime == 0) {
                process.turnaroundTime = currentTime - process.arrivalTime; // 반환 시간 계산
                process.waitingTime = process.turnaroundTime - process.burstTime; // 대기 시간 계산
                completedProcesses.add(process); // 완료된 프로세스 리스트에 추가
            } else {
                queue.add(process); // 남은 실행 시간이 있으면 큐에 다시 추가
            }
        }

        Print.printGanttChart(completedProcesses); // 간트 차트 출력
        Print.printProcessDetails(completedProcesses); // 프로세스 상세 정보 출력
    }
}

// 선점 SRT(Shortest Remaining Time) 스케줄링 알고리즘 클래스
class SRT {
    public void schedule(List<Process> processes) {
        List<Process> completedProcesses = new ArrayList<>(); // 완료된 프로세스 리스트
        processes.sort(Comparator.comparingInt(proc -> proc.arrivalTime)); // 도착 시간 기준으로 정렬
        PriorityQueue<Process> queue = new PriorityQueue<>(Comparator.comparingInt(proc -> proc.remainingTime)); // 남은 실행 시간 기준으로 우선순위 큐
        int currentTime = 0; // 현재 시간 초기화

        while (!processes.isEmpty() || !queue.isEmpty()) { // 프로세스 또는 큐가 비어있지 않을 때
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                queue.add(processes.remove(0)); // 도착한 프로세스를 큐에 추가
            }

            if (queue.isEmpty()) {
                currentTime = processes.get(0).arrivalTime; // 큐가 비어있으면 현재 시간을 다음 도착 시간으로 갱신
                continue;
            }

            Process process = queue.poll();
            if (process.remainingTime == process.burstTime) {
                process.responseTime = currentTime - process.arrivalTime; // 첫 실행 시 응답 시간 계산
            }
            currentTime++;
            process.remainingTime--;

            if (process.remainingTime == 0) {
                process.turnaroundTime = currentTime - process.arrivalTime; // 반환 시간 계산
                process.waitingTime = process.turnaroundTime - process.burstTime; // 대기 시간 계산
                completedProcesses.add(process); // 완료된 프로세스 리스트에 추가
            } else {
                queue.add(process); // 남은 실행 시간이 있으면 큐에 다시 추가
            }
        }

        Print.printGanttChart(completedProcesses); // 간트 차트 출력
        Print.printProcessDetails(completedProcesses); // 프로세스 상세 정보 출력
    }
}

// HRN(Highest Response Ratio Next) 스케줄링 알고리즘 클래스
class HRN {
    public void schedule(List<Process> processes) {
        List<Process> completedProcesses = new ArrayList<>(); // 완료된 프로세스 리스트
        int currentTime = 0; // 현재 시간 초기화

        while (!processes.isEmpty()) { // 프로세스가 비어있지 않을 때
            Process nextProcess = null;
            double highestResponseRatio = -1; // 가장 높은 응답 비율 초기화

            for (Process process : processes) {
                if (process.arrivalTime <= currentTime) {
                    double responseRatio = (double) (currentTime - process.arrivalTime + process.burstTime) / process.burstTime;
                    if (responseRatio > highestResponseRatio) {
                        highestResponseRatio = responseRatio;
                        nextProcess = process; // 가장 높은 응답 비율을 가진 프로세스 선택
                    }
                }
            }

            if (nextProcess == null) {
                currentTime++; // 선택된 프로세스가 없으면 현재 시간 증가
                continue;
            }

            processes.remove(nextProcess);
            nextProcess.responseTime = currentTime - nextProcess.arrivalTime; // 응답 시간 계산
            nextProcess.waitingTime = nextProcess.responseTime; // 대기 시간 = 응답 시간
            nextProcess.turnaroundTime = nextProcess.waitingTime + nextProcess.burstTime; // 반환 시간 = 대기 시간 + 실행 시간

            currentTime += nextProcess.burstTime; // 현재 시간 갱신
            completedProcesses.add(nextProcess); // 완료된 프로세스 리스트에 추가
        }

        Print.printGanttChart(completedProcesses); // 간트 차트 출력
        Print.printProcessDetails(completedProcesses); // 프로세스 상세 정보 출력
    }
}

// 출력 클래스
class Print {
    public static void printGanttChart(List<Process> processes) {
        System.out.print("Gantt Chart: ");
        System.out.println();
        int currentTime = 0;

        // 상단의 프로세스 표시
        for (Process p : processes) {
            System.out.print("|");
            System.out.print("P" + p.id);
            for (int i = 0; i < p.burstTime - 1; i++) {
                System.out.print(" ");
            }
        }
        System.out.println("|");

        // 아래의 시간 표시
        System.out.print("0");
        currentTime = 0;
        for (Process p : processes) {
            currentTime += p.burstTime;
            String spaces = " ".repeat(p.burstTime);
            System.out.print(spaces);
            System.out.print(currentTime);
        }
        System.out.println();
    }

    public static void printProcessDetails(List<Process> processes) {
        System.out.println("+----+--------------+--------------+--------------+");
        System.out.println("| ID | 대기 시간(ms) | 응답 시간(ms) | 반환 시간(ms) |");
        System.out.println("+----+--------------+--------------+--------------+");

        for (Process p : processes) {
            System.out.printf("| P%-1d | %-13d | %-13d | %-13d |\n", p.id, p.waitingTime, p.responseTime, p.turnaroundTime);
        }

        System.out.println("+----+--------------+--------------+--------------+");

        double avgWaitingTime = calculateAverageWaitingTime(processes); // 평균 대기 시간 계산
        double avgResponseTime = calculateAverageResponseTime(processes); // 평균 응답 시간 계산
        double avgTurnaroundTime = calculateAverageTurnaroundTime(processes); // 평균 반환 시간 계산

        System.out.println("+----+--------------+--------------+--------------+");
        System.out.printf("| 평균 | %-13.2f | %-13.2f | %-13.2f |\n", avgWaitingTime, avgResponseTime, avgTurnaroundTime);
        System.out.println("+----+--------------+--------------+--------------+");
    }

    private static double calculateAverageWaitingTime(List<Process> processes) {
        int totalWaitingTime = processes.stream().mapToInt(p -> p.waitingTime).sum();
        return (double) totalWaitingTime / processes.size(); // 평균 대기 시간 반환
    }

    private static double calculateAverageResponseTime(List<Process> processes) {
        int totalResponseTime = processes.stream().mapToInt(p -> p.responseTime).sum();
        return (double) totalResponseTime / processes.size(); // 평균 응답 시간 반환
    }

    private static double calculateAverageTurnaroundTime(List<Process> processes) {
        int totalTurnaroundTime = processes.stream().mapToInt(p -> p.turnaroundTime).sum();
        return (double) totalTurnaroundTime / processes.size(); // 평균 반환 시간 반환
    }
}

// 메인 클래스
public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            List<Process> processes = readProcessesFromFile("input.txt"); // 파일에서 프로세스 읽기

            System.out.println("다음 중 사용할 스케줄링 알고리즘을 선택하세요:");
            System.out.println("1. FCFS  2. SJF  3. 비선점 Priority  4. 선점 Priority");
            System.out.println("5. RR    6. SRT  7. HRN  8. 종료");
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
                case 3:
                    System.out.println("비선점 Priority 스케줄링:");
                    new NonPreemptivePriority().schedule(new ArrayList<>(processes));
                    break;
                case 4:
                    System.out.println("선점 Priority 스케줄링:");
                    new PreemptivePriority().schedule(new ArrayList<>(processes));
                    break;
                case 5:
                    System.out.println("RR 스케줄링:");
                    System.out.print("시간 할당량을 입력하세요: ");
                    int quantum = scanner.nextInt();
                    new RR(quantum).schedule(new ArrayList<>(processes));
                    break;
                case 6:
                    System.out.println("SRT 스케줄링:");
                    new SRT().schedule(new ArrayList<>(processes));
                    break;
                case 7:
                    System.out.println("HRN 스케줄링:");
                    new HRN().schedule(new ArrayList<>(processes));
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
            int processCount = Integer.parseInt(reader.readLine().trim()); // 첫 줄에서 프로세스 개수 읽기
            for (int i = 0; i < processCount; i++) {
                String[] tokens = reader.readLine().split(","); // 각 프로세스 정보는 쉼표로 구분
                int id = Integer.parseInt(tokens[0].trim());
                int arrivalTime = Integer.parseInt(tokens[1].trim());
                int burstTime = Integer.parseInt(tokens[2].trim());
                int priority = Integer.parseInt(tokens[3].trim());
                processes.add(new Process(id, arrivalTime, burstTime, priority)); // 프로세스 리스트에 추가
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
        return processes; // 프로세스 리스트 반환
    }
}
