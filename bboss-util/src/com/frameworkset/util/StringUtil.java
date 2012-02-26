/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                             *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.AccessController;
import java.sql.Blob;
import java.sql.Clob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.StringSubstitution;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.ObjectUtils;

import sun.security.action.GetPropertyAction;

/**
 * To change for your class or interface DAO中VOObject String类型与PO数据类型转换工具类.
 * 
 * @author biaoping.yin
 * @version 1.0
 */

public class StringUtil extends SimpleStringUtil {
	
	public static String getRealPath(HttpServletRequest request, String path) {
		String contextPath = request.getContextPath();

		if (contextPath == null) {
//			System.out.println("StringUtil.getRealPath() contextPath:"
//					+ contextPath);
			return path;
		}
		if (path == null) {
			return null;
		}
		if (path.startsWith("/") && !path.startsWith(contextPath + "/")) {
			if (!contextPath.equals("/"))
				return contextPath + path;
			else {
				return path;
			}

		} else {
			return path;
		}

	}
	
	

	public static String getParameter(HttpServletRequest request, String name,
			String defaultValue) {
		String value = request.getParameter(name);
		return value != null ? value : defaultValue;
	}

	public static void main(String args[]) {
//		String str = "中文,'bb,cc,'dd";
//		try {
//			str = new String(str.getBytes(), "utf-8");
//		} catch (UnsupportedEncodingException ex) {
//		}
//		System.out.println(str.getBytes()[0]);
//		System.out.println(str.getBytes()[1]);
//		System.out.println(str.getBytes()[2]);
//		System.out.println(str.getBytes()[3]);
//
//		System.out.println("?".getBytes()[0]);
		int maxlength = 16;
		String replace  ="...";
		String outStr = "2010年02月04日12时许，何金瑶（女、1987年06月18日生、身份证：430981198706184686、湖南省沅江市沅江市南大膳镇康宁村十二村民组24号）报警：其经营的益阳市电信对面的晴天服装店被盗了。接警后我所民警立即赶至现场了解系，今日中午12时许何金瑶与母亲黄志元在店内做生意，有两男子进入店内，其中一男子以搬店内的试衣镜出去吸引注意力。另一男子就进行盗窃，盗取了其店内收银台抽屉内700元人民币";
		
		System.out.println(StringUtil.getHandleString(maxlength,replace,false,false,outStr));
		
outStr = "2010年02月07日11时许，周灵颖报警：在2路公交车上被扒窃，并抓获一名嫌疑人。民警出警后，经调查，周灵颖于当日10时40分许坐2路车到桥南，途中被二名男子扒窃现金3100元。一名被当场抓获，另一名已逃走。 ";
		
		System.out.println(StringUtil.getHandleString(maxlength,replace,false,false,outStr));
	}

	
	
	public static void sendFile(HttpServletRequest request, HttpServletResponse response, File file) throws Exception {
        OutputStream out = null;
        RandomAccessFile raf = null;
        try {
        	raf = new RandomAccessFile(file, "r");
        	out =  response.getOutputStream();
            long fileSize = raf.length();
            long rangeStart = 0;
            long rangeFinish = fileSize - 1;

            // accept attempts to resume download (if any)
            String range = request.getHeader("Range");
            if (range != null && range.startsWith("bytes=")) {
                String pureRange = range.replaceAll("bytes=", "");
                int rangeSep = pureRange.indexOf("-");

                try {
                    rangeStart = Long.parseLong(pureRange.substring(0, rangeSep));
                    if (rangeStart > fileSize || rangeStart < 0) rangeStart = 0;
                } catch (NumberFormatException e) {
                    // ignore the exception, keep rangeStart unchanged
                }

                if (rangeSep < pureRange.length() - 1) {
                    try {
                        rangeFinish = Long.parseLong(pureRange.substring(rangeSep + 1));
                        if (rangeFinish < 0 || rangeFinish >= fileSize) rangeFinish = fileSize - 1;
                    } catch (NumberFormatException e) {
                        // ignore the exception
                    }
                }
            }

            // set some headers


            response.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes(),"ISO-8859-1").replaceAll(" ", "-"));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", Long.toString(rangeFinish - rangeStart + 1));
            response.setHeader("Content-Range", "bytes " + rangeStart + "-" + rangeFinish + "/" + fileSize);

            // seek to the requested offset
            raf.seek(rangeStart);

            // send the file
            byte buffer[] = new byte[1024];

            long len;
            int totalRead = 0;
            boolean nomore = false;
            while (true) {
                len = raf.read(buffer);
                if (len > 0 && totalRead + len > rangeFinish - rangeStart + 1) {
                    // read more then required?
                    // adjust the length
                    len = rangeFinish - rangeStart + 1 - totalRead;
                    nomore = true;
                }

                if (len > 0) {
                    out.write(buffer, 0, (int) len);
                    totalRead += len;
                    if (nomore) break;
                } else {
                    break;
                }
            }
            out.flush();
        } 
        catch(Exception e)
        {
        	throw e;
        }
        finally {
            try
			{
				raf.close();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            try
			{
				out.close();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
 
 public static void sendFile(HttpServletRequest request, HttpServletResponse response, String filename,Blob blob) throws Exception {
	 if(blob == null)
		 return ;
	 sendFile_( request,  response,  filename,blob.getBinaryStream(),blob.length());
 }
 
 public static void sendFile(HttpServletRequest request, HttpServletResponse response, String filename,Clob clob) throws Exception {
	 if(clob == null)
		 return ;
	 sendFile_( request,  response,  filename,clob.getAsciiStream(),clob.length());
 }
 
 public static void sendFile_(HttpServletRequest request, HttpServletResponse response, String filename,InputStream in,long fileSize) throws Exception {
        OutputStream out = null;
//        InputStream in = null;
        try {
        	if(in == null)
        		return;
        	out = response.getOutputStream();
        	
//        	if(blob == null)
//        		return ;
//        	in = blob.getBinaryStream();
//            long fileSize = blob.length();
            long rangeStart = 0;
            long rangeFinish = fileSize - 1;

            // accept attempts to resume download (if any)
            String range = request.getHeader("Range");
            if (range != null && range.startsWith("bytes=")) {
                String pureRange = range.replaceAll("bytes=", "");
                int rangeSep = pureRange.indexOf("-");

                try {
                    rangeStart = Long.parseLong(pureRange.substring(0, rangeSep));
                    if (rangeStart > fileSize || rangeStart < 0) rangeStart = 0;
                } catch (NumberFormatException e) {
                    // ignore the exception, keep rangeStart unchanged
                }

                if (rangeSep < pureRange.length() - 1) {
                    try {
                        rangeFinish = Long.parseLong(pureRange.substring(rangeSep + 1));
                        if (rangeFinish < 0 || rangeFinish >= fileSize) rangeFinish = fileSize - 1;
                    } catch (NumberFormatException e) {
                        // ignore the exception
                    }
                }
            }

            // set some headers


            response.setHeader("Content-Disposition", "attachment; filename=" + new String(filename.getBytes(),"ISO-8859-1").replaceAll(" ", "-"));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", Long.toString(rangeFinish - rangeStart + 1));
            response.setHeader("Content-Range", "bytes " + rangeStart + "-" + rangeFinish + "/" + fileSize);

            // seek to the requested offset
            

            // send the file
            byte buffer[] = new byte[1024];
            in.skip(rangeStart);
            long len;
            int totalRead = 0;
            boolean nomore = false;
            while (true) {
                len = in.read(buffer);
                if (len > 0 && totalRead + len > rangeFinish - rangeStart + 1) {
                    // read more then required?
                    // adjust the length
                    len = rangeFinish - rangeStart + 1 - totalRead;
                    nomore = true;
                }

                if (len > 0) {
                    out.write(buffer, 0, (int) len);
                    totalRead += len;
                    if (nomore) break;
                } else {
                    break;
                }
            }
            out.flush();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	throw e;
        }
        finally {
        	try
			{
        		if(in != null)
        			in.close();		
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try
			{
            	if(out != null)
            		out.close();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
 
 	
}
