/**
 * Copyright
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kth.common.utils.etc;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * @Author   : basagee
 * @FileName : Serialization.java
 * @Project  : DynamicLoading
 * @Author   : basagee
 * @Date     : 2011. 11. 18.
 * @Revision :
 * @Description :
 */
public class SerializationUtil {

    /**
     * @Method Name : serializeObject
     * @Date     : 2011. 11. 18.
     * @Author   : basagee
     * @Revision :
     * @Description : 오브젝트는 바이트어레이로 변환한다. DB 저장등을 위하여 바이트어레이를 사용하는 경
     * @param o
     * @return  성공하면 바이트 어레이를 돌려주며, 실패하면 null을 반환한다.
     */
    public static byte[] serializeObject(Object object) { 
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
     
        try { 
            ObjectOutput out = new ObjectOutputStream(bos); 
            out.writeObject(object); 
            out.close(); 
     
            // Get the bytes of the serialized object 
            byte[] buf = bos.toByteArray(); 
     
            return buf; 
        } catch(IOException ioe) { 
            Log.e("serializeObject", "error", ioe); 
     
            return null; 
        } 
    } 
    
    /**
     * @Method Name : serializeObject
     * @Date     : 2011. 11. 18.
     * @Author   : basagee
     * @Revision :
     * @Description : 객체를 직렬화하여 파일에 저장한다.
     * @param destFile 저장될 File
     * @param object 직렬화 하고자 하는 객
     * @return 성공하면 true, 실패하면 false를 반환한다.
     */
    public static boolean serializeObject(File destFile, Object object) { 
     
        try { 
            FileOutputStream fos = new FileOutputStream(destFile);

            ObjectOutput out = new ObjectOutputStream(fos); 
            out.writeObject(object); 
            out.close(); 
        } catch(IOException ioe) { 
            Log.e("serializeObject", "error", ioe); 
     
            return false; 
        } 
        return true;
    } 
    
    /**
     * @Method Name : deserializeObject
     * @Date     : 2011. 11. 18.
     * @Author   : basagee
     * @Revision :
     * @Description : 바이트어레이를 입력받아 Desirialized 된 Object를 반환한다.
     * @param b     오브젝트의 바이트 어레
     * @return      성공하면 오브젝트를 반환하고, 실패하면 null을 반환한다.
     */
    public static Object deserializeObject(byte[] b) { 
        try { 
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b)); 
            Object object = in.readObject(); 
            in.close(); 
     
            return object; 
        } catch(ClassNotFoundException cnfe) { 
            Log.e("deserializeObject", "class not found error", cnfe); 
     
            return null; 
        } catch(IOException ioe) { 
            Log.e("deserializeObject", "io error", ioe); 
     
            return null; 
        } catch(Exception e) { 
            Log.e("deserializeObject", "io error", e); 
     
            return null; 
        } 
    }

    /**
     * @Method Name : deserializeObject
     * @Date     : 2011. 11. 18.
     * @Author   : basagee
     * @Revision :
     * @Description : 전달받은 파일로부터 Object를 Desirialize 한다.
     * @param srcFile
     * @return  성공하면 오브젝트를 반환하며, 실패하면 null을 반환한다.
     */
    public static Object deserializeObject(File srcFile) { 
        try { 
            FileInputStream fis = new FileInputStream(srcFile);
            
            ObjectInputStream in = new ObjectInputStream(fis); 
            Object object = in.readObject(); 
            in.close(); 
     
            return object; 
        } catch(ClassNotFoundException cnfe) { 
            Log.e("deserializeObject", "class not found error", cnfe); 
     
            return null; 
        } catch(IOException ioe) { 
            Log.e("deserializeObject", "io error", ioe); 
     
            return null; 
        } catch(Exception e) { 
            Log.e("deserializeObject", "io error", e); 
     
            return null; 
        } 
    }
}
