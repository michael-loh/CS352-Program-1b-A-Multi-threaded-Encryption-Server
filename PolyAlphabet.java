
public class PolyAlphabet {

	private int c1;
	private int c2;

	public PolyAlphabet(int c1, int c2){
		this.c1 = c1;
		this.c2 = c2;
	}

	public String encrypt(String cleartext){
		String ciphertext = "";
		int charTrack = 0; //need a variable to keep track of what Caesar cipher to use.
		for(int i = 0; i < cleartext.length(); i++){

			int j = charTrack % 5;
			if(j == 0 || j == 3){					//use c1
				ciphertext += getCipherChar(cleartext.charAt(i), c1);
			}
			else if(j == 1 || j == 2 || j == 4){	//use c2
				ciphertext += getCipherChar(cleartext.charAt(i), c2);
			}

			int asc = cleartext.charAt(i);
			if((asc >= 'A' && asc <= 'Z') || (asc >= 'a' && asc <= 'z')){
				charTrack++;
			}
		}
		return ciphertext;
	}

	private char getCipherChar(char ch, int c){
		int asc;
		if(ch >= 'a' && ch <= 'z'){	//if upper case
			asc = ch - (int)'a';
			asc += c;
			asc = (asc%26) + (int) 'a';
			return (char)asc;
		}
		else if(ch >= 'A' && ch <= 'Z'){			//if lower case
			asc = ch - (int)'A';
			asc += c;
			asc = (asc%26) + (int) 'A';
			return (char)asc;
		}
		else{
			return ch;
		}
	}
}
