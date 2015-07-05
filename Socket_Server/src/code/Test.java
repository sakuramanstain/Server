package code;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatabaseManager m_DatabaseManager=new DatabaseManager();
    	m_DatabaseManager.Initial();
    	System.out.println(m_DatabaseManager.get_nadsfal(1));
	}

}
