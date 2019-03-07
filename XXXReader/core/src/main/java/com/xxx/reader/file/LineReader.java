package com.xxx.reader.file;

import java.io.IOException;

public interface LineReader {
      static int FORWARD = 0;
      static int BACKWARD = 1;

      String m_readline() throws IOException;

      void findLastLine() throws IOException;

      void setOffset(long o, boolean isOpen) throws IOException;

      long getOffset();

      String openfile() throws IOException;

      void closefile();


      long getLocation() throws IOException;

      long getSize() throws IOException;

      long getFileEndPos() throws IOException;

      String getFileName();

      void m_seekNextLine() throws IOException;
}
