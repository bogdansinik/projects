package psa.naloga4;

//import java.util.Vector;


public class BinomialHeap {
	BinomialNode[] data;
	
	BinomialHeap(){
		data = new BinomialNode[1];
	
	}
	
	public boolean insert(int key){
		BinomialNode noud = new BinomialNode(key);
		BinomialNode looper = data[0];
		int coun = 0;
		while (looper!=null && coun<data.length){
			noud=merge(noud,looper);
			data[coun]=null;
			coun+=1;
			if(coun!=data.length) {
			looper=data[coun];
		}
		}
		if (coun==data.length){
			this.resizeArray();
			data[data.length/2]=noud;
			data[data.length/2-1]=null;
			return true;
		}
		else {
			if (coun>0){
			data[coun]=noud;
			data[coun-1]=null;
			return true;
			}
		
		
			else{
				data[coun]=noud;
				return true;
			}
		}
		}
	
		
	
	public int getMin() {
		int j=Integer.MAX_VALUE;
		for(int i=0;i<this.data.length;i++) {
			if (data[i]!=null) {
				if((data[i].getKey())<j) {
					j=data[i].getKey();
				}
			}
		}
	return j;
	}
	
	public boolean delMin(){
		int j=this.getMin();
		int k=0;
		BinomialNode[] children = null;
		if(j==Integer.MAX_VALUE) {
			return false;
		}
		else {
		boolean small=false;
		while(small!=true) {
		if(data[k]!=null) {
			if(data[k].getKey()==j) {
				if(k==0){
					data[k]=null;
					small=true;
					return true;
				}
				else {
					children = new BinomialNode[k];
					for(int z=0;z<k;z++) {
						children[z]=data[k].childs.get(z);
						
					}
					small=true;
					data[k]=null;
					for(int r=0;r<k;r++) {
						int q=r;
						BinomialNode noud  = children[children.length-1-r];
						BinomialNode looper= data[q];
						while(looper!=null) {
							noud =merge(noud,data[q]);
							data[q]=null;
							q+=1;
							looper=data[q];
						}
						data[q]=noud;
					}
						return true;
					
					
					
				}
			}
			else {
				k+=1;
			}
		
		}
			else {
				k+=1;
			}
		
		}
		}
		return false;
		}
		
		
		
	private void resizeArray() {
		BinomialNode[] newarr= new BinomialNode[2*(data.length)];
		for(int i=0;i<(data.length);i++){
			newarr[i]=data[i];
			}
		data = newarr;
	
	}

	
	private BinomialNode merge(BinomialNode t1, BinomialNode t2) {
		if(t1.getKey()<t2.getKey()) {
			t1.childs.add(0, t2);
			return t1;
		}
		t2.childs.add(0, t1);
		return t2;
	}
}

