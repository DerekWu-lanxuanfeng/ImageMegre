package com.flame.tools.megreimage.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.test.TestFileChanel.java
 * @Description: FileChannel 测试类
 * @Create: DerekWu  2016年5月6日 下午4:05:47 
 * @version: V1.0  
 * 
 * FileChanel是NIO中的类,NIO称为新 I/O,使用了更接近操作系统执行I/O的方式:通道和缓冲器.
 * 
 * 即先打开一个通道,然后创建一个缓冲器,将通道和缓冲器关联.通道是双向的,既可以读也可以写.
 * 从通道中读取数据时,先把数据读入缓冲器,然后从缓冲器中获取数据;
 * 往通道中写数据时,先把数据写入缓冲器,然后再从缓冲器中将数据写入通道.
 * 
 * 新I/O出来后,还修改了旧IO包中的三个类,FileInputStream,FileOutputStream和RandomAccessFile.
 * 通过这三个类的getChannel()返回一个FileChannel通道.
 * 
 * 注意,上文说了Channel是双向的,即,可以读,也可以写(FileChannel有read和write方法).
 * 这个对于RandomAccessFile返回的FileChannel来说,不是很奇怪.
 * 
 * 但是对于 使用 FileInputStream 返回的FileChannel来写(write),
 * 或者使用FileOutputStream返回的FileChannel来读(read)
 * 的话,就有些奇怪了.
 * 
 * 为了排除疑问,使用下面的代码测试之.
 * 测试结果:
 * (1)FileInputStream 生成的FileChannel是只读的,尝试写入是抛异常:NonWritableChannelException
 * (2)FileOutputStream 生成的FileChannel只能写入,并且一旦建立这个对象,则与之关联的文件内容被清空.
 * 如果尝试读取获取的FileChannel的内容,则抛异常:NonReadableChannelException
 * (3)由RandomAccessFile获取的FileChannel则仍旧可读可写.
 *
 */

public class TestFileChanel
{
  public static void main(String[] args) throws IOException
  {
    boolean bInputChannel = false;
    boolean bOutputchannel = false;
    boolean bRandomAccessChannel = true;

    // 首先,使用RandomAccessFile创建一个4*4字节的文件(相当于存入4个int型的0)
    File f = new File("D:\\D\\test_channel.dat");
    // 如果文件存在,先删除之,比较暴力,需要确保D:\\D\\test_channel.dat 这个文件不存在
    if (f.exists())
    {
      f.delete();
    }

    RandomAccessFile rf = new RandomAccessFile(f, "rw");
    rf.setLength(4 * 4);  //4 个 int 的容量
    rf.writeInt(1); //前4个字节 写入整型的 1
    rf.seek(3 * 4); //position移动到第3个整型(3*4 = 12)之后,
    rf.writeInt(5);// 最后4个字节 写入一个整型的5
    rf.close();
    // 完成之后,使用UE打开这个二进制文件,可见文件结构如下(|分隔符不存在的,这里为了方便阅读):
    // 00 00 00 01 | 00 00 00 00 | 00 00 00 00 | 00 00 00 05

    FileChannel fc = new RandomAccessFile(f, "rw").getChannel();
    if (bRandomAccessChannel)
    {
      System.out.println("------使用RandomAccessFile返回的FileChannel--------");
      // 测试使用FileOutputStream返回的FileChannel 从 上面文件的文件中 读取 一个int(4字节)
      fc = new RandomAccessFile(f, "rw").getChannel();
      try
      {
        fc.position(4);// 文件通道位置设置到第5个字节之前,第4个字节之后
        //00 00 00 01 $ 00 00 00 00 | 00 00 00 00 | 00 00 00 05
        //此时position 在上面的$处,注意 这里是文件的position
        
        ByteBuffer ib = ByteBuffer.allocate(8);//分配一个buffer 8字节(capacity = 8),且全部初始化为0了
        //可以认为此时的buffe结构向下面这个样子
        // $ 00 00 00 01 | 00 00 00 00
        // $为初始化后position位置, | 是分隔符 便于阅读,此时limit等于capacity
        
        System.out.printf("buffer ByteBuffer.allocate(8) 初始化完毕之后%nposition=%1$d,limit=%2$d %n",ib.position(),ib.limit());

        // 先写一个整数7
        System.out.printf("buffer 在put之前,position=%1$d %n",ib.position());
        ib.asIntBuffer().put(7);
        System.out.printf("buffer 在put之后,position=%1$d %n",ib.position());
        //此时buffer格式为:
        //$ 00 00 00 07 | 00 00 00 00
        //注意:此时buffer的position并没有因为put而向前移动了,仍然在开头位置.
        
        ib.limit(ib.position() + 4);
        //限制只写入buffer中的00 00 00 07 这个数据,即写入整型的7到文件
        fc.write(ib);
        System.out.println("写入后,buffer的position:" + ib.position());//写入后,buffer的position:4
        //此时buffer格式为:
        // 00 00 00 07 $ 00 00 00 00
        // 注意,因Channel 将Buffer中的的数据写入,
        //导致写入后buffer的position从之前的位置(0)移动了一个limit的位置(4)
        
        fc.force(true);//强制将所有对此通道的文件更新写入包含该文件的存储设备中。
        System.out.printf("写入完成后,文件通道位置为:%1$d,buffer位置为:%2$d %n" , fc.position(),ib.position());
        //写入完成后,文件通道位置为:8,buffer位置为:4
        //写入后,文件的二进制数据如下:
        //00 00 00 01 | 00 00 00 07 $ 00 00 00 00 | 00 00 00 05
        //写入完成后,buffer数据如下:
        //00 00 00 07 $ 00 00 00 00

        System.out.printf("调用clear()之 前,buffer位置为:%1$d,limit为:%2$d %n" ,ib.position(),ib.limit());
        ib.clear(); //清空buffer,将位置设置为 0，将限制设置为容量，并丢弃标记
        
        // 测试从通道中读一个整数
        System.out.printf("调用clear()之后,buffer位置为:%1$d,limit为:%2$d %n" ,ib.position(),ib.limit());
        //调用clear()之后,buffer位置为:0,limit为:8
        //此时buffer数据如下:
        // $ 00 00 00 00 | 00 00 00 00
        fc.read(ib);
        
        
        //read的时候从Channel(fc)的当前位置开始read,此时Channel(fc)的位置为8
        System.out.printf("调用fc.read(ib)读取之后,buffer位置为:%1$d %n" ,ib.position());
        //调用fc.read(ib)读取之后,buffer位置为:8
        //注意read之后,如果文件内容大于buffer,read limit - position 个字节,并从position填充到limit
        //此时buffer内容为:
        // 00 00 00 00 | 00 00 00 05 $
        
        System.out.printf("调用fc.read(ib)读取之后,文件通道fc的位置为:%1$d %n" ,fc.position());
        //
        
        ib.position(4);
        //将buffer的position提前4,读取最后一个整数,否则报java.nio.BufferUnderflowException 异常
        //此时buffer内容为:
        // 00 00 00 00 $ 00 00 00 05
        int iTmp = ib.asIntBuffer().get();//读取了00 00 00 05
        System.out.printf("调用ib.asIntBuffer().get()之后,buffer位置为:%1$d %n" ,ib.position());
        //调用ib.asIntBuffer().get()之后,buffer位置为:4 
        System.out.println(iTmp); //输出5

      }
      catch (NonReadableChannelException e)
      {
        System.out.println("通道不可读");
      }
      finally
      {
        fc.close();
      }
    }
    
    if (bInputChannel)
    {
      System.out.println("------使用FileInputStream返回的FileChannel--------");
      // 接下来,测试使用FileInputStream返回的FileChannel往上面文件的文件中写入一个int(4字节)
      fc = new FileInputStream(f).getChannel();
      try
      {
        fc.position(4);// 通道位置设置到第5个字节之前,第4个字节之后
        ByteBuffer ib = ByteBuffer.allocate(4);

        // 测试从通道中读一个整数
        int iTmp = ib.asIntBuffer().get();
        System.out.println(iTmp); // 可以读出一个int

        ib.clear();

        ib.asIntBuffer().put(7);
        fc.write(ib); // 执行时报错,抛NonWritableChannelException异常,
        //说明FileInputStream(f).getChannel()方法获取的通道是不可写的.

      }
      catch (NonWritableChannelException e)
      {
        System.out.println("通道不可写");
      }
      finally
      {
        fc.close();
      }
    }
    
    if (bOutputchannel)
    {
      System.out.println("------FileOutputStream返回的FileChannel--------");
      fc = new FileOutputStream(f).getChannel();//调用这个之后,原来的文件内容被清空了,等着write
      
      // 测试使用FileOutputStream返回的FileChannel 从 上面文件的文件中 读取 一个int(4字节)
      try
      {
        fc.position(4);// 通道位置设置到第5个字节之前,第4个字节之后
        ByteBuffer ib = ByteBuffer.allocate(16);

        // 先写一个整数7
        ib.asIntBuffer().put(7);
        fc.write(ib);
        fc.force(true);
        ib.flip();

        ib.clear();
        // 测试从通道中读一个整数
        fc.read(ib); // 抛出异常 NonReadableChannelException
      }
      catch (NonReadableChannelException e)
      {
        System.out.println("通道不可读");
      }
      finally
      {
        fc.close();
      }
    }
  }
}
