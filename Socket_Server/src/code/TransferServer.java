package code;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
 * ��������
 * �������������
 * */



public class TransferServer {

    private int defaultBindPort = 1818;    //Ĭ�ϼ����˿ں�Ϊ10000
    private int tryBindTimes = 0;           //��ʼ�İ󶨶˿ڵĴ����趨Ϊ0
    private ServerSocket serverSocket;      //�����׽��ֵȴ��Է������Ӻ��ļ�����
    private ExecutorService executorService;    //�̳߳�
    private final int POOL_SIZE = 4;            //����CPU���̳߳ش�С 
    public static final String PATH = "F:" +File.separator+ "workspace" + File.separator;
    /**
     * ���������Ĺ�������ѡ��Ĭ�ϵĶ˿ں�
     * @throws Exception
     */
    public TransferServer() throws Exception{
        try {
            this.bingToServerPort(defaultBindPort);
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
            System.out.println("�����߳��� �� " + Runtime.getRuntime().availableProcessors() * POOL_SIZE);
        } catch (Exception e) {
            throw new Exception("�󶨶˿ڲ��ɹ�!");
        }
    }
    
    /**
     * �������Ĺ�������ѡ���û�ָ���Ķ˿ں�
     * @param port
     * @throws Exception
     */
    public TransferServer(int port) throws Exception{
        try {
            this.bingToServerPort(port);
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
        } catch (Exception e) {
            throw new Exception("�󶨶˿ڲ��ɹ�!");
        }
    }
    
    private void bingToServerPort(int port) throws Exception{
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(port);
            System.out.println("��������!");
        } catch (Exception e) {
            this.tryBindTimes = this.tryBindTimes + 1;
            port = port + this.tryBindTimes;
            if(this.tryBindTimes >= 20){
                throw new Exception("���Ѿ����Ժܶ���ˣ��������޷��󶨵�ָ���Ķ˿�!������ѡ��󶨵�Ĭ�϶˿ں�");
            }
            //�ݹ�󶨶˿�
            this.bingToServerPort(port);
        }
    }
    
    public void service(){
        Socket socket = null;
        while (true) {
            try {
                socket = serverSocket.accept();
                executorService.execute(new HandleDeal(socket));////�󶨴���Ԫ
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
   
 }//TransferServer

