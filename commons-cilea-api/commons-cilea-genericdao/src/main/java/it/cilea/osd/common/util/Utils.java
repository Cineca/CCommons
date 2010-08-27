package it.cilea.osd.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class
 * 
 * @author pascarelli
 * 
 */
public class Utils {
	
	/**
	 * Compare two objects according to Collection semantics.
	 * 
	 * @param o1
	 *            the first object
	 * @param o2
	 *            the second object
	 * @return o1 == o2 || (o1 != null && o1.equals(o2))
	 */
	public static final boolean equals(Object o1, Object o2) {
		return o1 == o2 || (o1 != null && o1.equals(o2));
	}

	/**
	 * 
	 * Copy stream source to output destination in buffered mode, finally flush output stream.
	 * 
	 * @param source input stream
	 * @param destination output stream
	 * @throws IOException
	 */
	public static void bufferedCopy(final InputStream source,
			final OutputStream destination) throws IOException {
		final BufferedInputStream input = new BufferedInputStream(source);
		final BufferedOutputStream output = new BufferedOutputStream(
				destination);
		copy(input, output);
		output.flush();
	}

	/**
	 * Copy stream source 
	 * 
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static void copy(final InputStream input, final OutputStream output)
			throws IOException {
		final int BUFFER_SIZE = 1024 * 4;
		final byte[] buffer = new byte[BUFFER_SIZE];

		while (true) {
			final int count = input.read(buffer, 0, BUFFER_SIZE);

			if (-1 == count) {
				break;
			}

			// write out those same bytes
			output.write(buffer, 0, count);
		}

		// needed to flush cache
		// output.flush();
	}

}
