package code;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
 * 服务器类
 * 服务的总体流程
 * */



public class TransferServer {

    private int defaultBindPort = 1818;    //默认监听端口号为10000
    private int tryBindTimes = 0;           //初始的绑定端口的次数设定为0
    private ServerSocket serverSocket;      //服务套接字等待对方的连接和文件发送
    private ExecutorService executorService;    //线程池
    private final int POOL_SIZE = 4;            //单个CPU的线程池大小 
    public static final String PATH = "F:" +File.separator+ "workspace" + File.separator;
    /**
     * 不带参数的构造器，选用默认的端口号
     * @throws Exception
     */
    public TransferServer() throws Exception{
        try {
            this.bingToServerPort(defaultBindPort);
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
            System.out.println("开辟线程数 ： " + Runtime.getRuntime().availableProcessors() * POOL_SIZE);
        } catch (Exception e) {
            throw new Exception("绑定端口不成功!");
        }
    }
    
    /**
     * 带参数的构造器，选用用户指定的端口号
     * @param port
     * @throws Exception
     */
    public TransferServer(int port) throws Exception{
        try {
            this.bingToServerPort(port);
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
        } catch (Exception e) {
            throw new Exception("绑定端口不成功!");
        }
    }
    
    private void bingToServerPort(int port) throws Exception{
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(port);
            System.out.println("服务启动!");
        } catch (Exception e) {
            this.tryBindTimes = this.tryBindTimes + 1;
            port = port + this.tryBindTimes;
            if(this.tryBindTimes >= 20){
                throw new Exception("您已经尝试很多次了，但是仍无法绑定到指定的端口!请重新选择绑定的默认端口号");
            }
            //递归绑定端口
            this.bingToServerPort(port);
        }
    }
    
    public void service(){
        Socket socket = null;
        while (true) {
            try {
                socket = serverSocket.accept();
                executorService.execute(new HandleDeal(socket));////绑定处理单元
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
   
 }//TransferServer

