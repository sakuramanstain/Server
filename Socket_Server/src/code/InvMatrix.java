package code;

public class InvMatrix {
	private int N;
	double[][] src;
	double[][] result;
	public double[][] Inv(double[][] m_src,int m_N) {
		N=m_N;
		src=m_src;
		result = new double[N][N];
		System.out.print("‘¥æÿ’Û");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(src[i][j] + ", ");
			}
			System.out.print("\t ");
			for (int j = 0; j < N; j++) {
				result[i][i] = 1;
				
			}
			System.out.println();
		}
		calCol(0);
		calColBack(N - 1);
		reInit();
		System.out.print("ƒÊæÿ’Û");
		for (int i = 0; i < N; i++) {
			/*for (int j = 0; j < N; j++) {
				System.out.print(src[i][j] + ", ");
			}
			System.out.print("\t ");*/
			for (int j = 0; j < N; j++) {
				System.out.print(result[i][j] + ", ");
			}
			System.out.println();
		}
		return result;
	}

	private void reInit() {
		for (int i = 0; i < N; i++) {
			double coefficient = 1 / src[i][i];
			src[i][i] = 1;
			for (int j = 0; j < N; j++)
				result[i][j] *= coefficient;
		}
	}

	private void calColBack(int col) {
		for (int i = col - 1; i >= 0; i--) {
			//System.out.println("--------------------------");
			double coefficient = -1 * src[i][col] / src[col][col];
			for (int z = 0; z < N; z++) {
				src[i][z] += coefficient * src[col][z];
				result[i][z] += coefficient * result[col][z];
			}
			/*for (int h = 0; h < N; h++) {
				for (int j = 0; j < N; j++) {
					System.out.print(src[h][j] + ", ");
				}
				System.out.print("\t ");
				for (int j = 0; j < N; j++) {
					System.out.print(result[h][j] + ", ");
				}
				System.out.println();
			}	*/
		}
		if (col > 0)
			calColBack(col - 1);
	}

	private void calCol(int col) {
		if(src[col][col]==0.0){
			int h;
			for(h=col+1;h<N;h++){
				if(src[h][col]!=0.0){
					double[] tem=new double[N];
					tem=src[col];
					src[col]=src[h];
					src[h]=tem;
					
					double[] tem1=new double[N];
					tem1=result[col];
					result[col]=result[h];
					result[h]=tem1;
					
					break;
				}
			}
			if(h==N){
				System.out.println("æÿ’Û≤ªø…ƒÊ£°£°");
				System.exit(0);
			}
		}
		for (int i = col + 1; i < N; i++) {
			double coefficient = -1 * src[i][col] / src[col][col];
			//System.out.println(coefficient);
			for (int z = 0; z < N; z++) {
				src[i][z] =src[i][z]+ coefficient*src[col][z];
				//System.out.println("11--"+src[i][z]);
				result[i][z] = result[i][z]+coefficient*result[col][z];
				//System.out.println("22--"+result[i][z]);
			}
			/*for (int h = 0; h < N; h++) {
				for (int j = 0; j < N; j++) {
					System.out.print(src[h][j] + ", ");
				}
				System.out.print("\t ");
				for (int j = 0; j < N; j++) {
					System.out.print(result[h][j] + ", ");
				}
				System.out.println();
			}	*/
		}
		
		
		if (col + 1 < N)
			calCol(col + 1);
	}
}
