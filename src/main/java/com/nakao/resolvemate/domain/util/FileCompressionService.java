package com.nakao.resolvemate.domain.util;

import com.nakao.resolvemate.domain.exception.FileHandlingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class FileCompressionService {

    /**
     * Compresses the given data using Deflater with best compression level.
     *
     * @param data the data to compress
     * @return the compressed data as byte array
     * @throws FileHandlingException if there is an error compressing the data
     */
    public static byte[] compressData(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            throw new FileHandlingException("Error compressing file");
        }

        return outputStream.toByteArray();
    }

    /**
     * Decompresses the given data using Inflater.
     *
     * @param data the data to decompress
     * @return the decompressed data as byte array
     * @throws FileHandlingException if there is an error decompressing the data
     */
    public static byte[] decompressData(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            throw new FileHandlingException("Error decompressing file");
        }

        return outputStream.toByteArray();
    }

}
