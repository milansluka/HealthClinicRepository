package ehc.util;
import java.io.*;


public class CodepageConverter {

	static void convert(String src, String fromCp, String dest, String toCp) {
		try {
			convert(new FileInputStream(src), fromCp, new FileOutputStream(dest), toCp);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	static void convert(InputStream fis, String fromCp, OutputStream fos, String toCp) {
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;

		try {
			char buffer[] = new char[0xffff];
			int nbytes;

			isr = new InputStreamReader(fis, fromCp);
			osw = new OutputStreamWriter(fos, toCp);

			while ((nbytes = isr.read(buffer)) != (-1))
				osw.write(buffer, 0, nbytes);

			osw.flush();
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if (isr != null)
				try {
					isr.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			if (osw != null)
				try {
					osw.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			if (fos != null)
				try {

					fos.close();
				} catch (IOException e) {
					System.err.println(e);
				}
		}
	}

	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("Usage: java CodepageConverter <src> <dest> <fromCp> <toCp>");
			System.out.println("       java CodepageConverter EBCFile.data ASCFile.txt Cp1141 ISO8859_1");
			System.out.println("       default - fromCp: Cp1141");
			System.out.println("                 toCp  : ISO8859_1");
		}
		else {
			String cpFrom = args.length >= 3 ? args[2] : "Cp1141";
			String cpTo = args.length == 4 ? args[3] : "ISO8859_1";
			convert(args[0], cpFrom, args[1], cpTo);
		}
	}

}