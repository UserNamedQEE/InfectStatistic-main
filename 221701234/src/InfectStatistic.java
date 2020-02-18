import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.nio.charset.StandardCharsets;


class InfectStatistic {
    public static void main(String[] args) 
    {
        //用来保存路径以及日期
        String log_Path,out_Path,date;
        log_Path="";
        out_Path="";
        date="";
        ArrayList<String> type,province;
        type=new ArrayList<String>();
        province=new ArrayList<String>();
        if(args.length==0||!args[0].equals("list"))
        {
            System.out.println("您输入的命令有误，请检查后重新输入");
        }
        else
        {
            //用来从命令中获取所需参数
            for(int i=1;i<args.length;++i)
            {
                if(args[i].equals("-log"))
                {
                    log_Path=args[++i];
                }
                else if(args[i].equals("-out"))
                {
                    out_Path=args[++i];
                }
                else if(args[i].equals("-date"))
                {
                    date=args[++i];
                }
                else if(args[i].equals("-type"))
                {
                    for(++i;i<args.length;++i)
                    {
                        if(!args[i].equals("-province"))
                        {
                        type.add(args[i]);
                        }
                        else 
                        {
                            --i;
                            break;
                        }
                    }
                }
                else if(args[i].equals("-province"))
                {
                    for(++i;i<args.length;++i)
                    {
                        if(!args[i].equals("-type"))
                        {
                        province.add(args[i]);
                        }
                        else 
                        {
                            --i;
                            break;
                        }
                    } 
                }
            }

            //判断是否输入相关路径
            if(log_Path.length()==0||out_Path.length()==0)
            {
                System.out.println("输入的命令缺少必要参数");
                return;
            }
            
            //用于统计全国的数据
            int ip,sp,cure,dead;
            ip=0;
            sp=0;
            cure=0;
            dead=0;
            //预先初始化后面用到的hashmap
            HashMap<String,Integer> p_list=new HashMap<>();
            HashMap<String,Integer> t_list=new HashMap<>();
            HashMap<String,Integer> ip_list=new HashMap<>();
            HashMap<String,Integer> sp_list=new HashMap<>();
            HashMap<String,Integer> cure_list=new HashMap<>();
            HashMap<String,Integer> dead_list=new HashMap<>();
            if(province.size()!=0)
            {
                for(String p:province)
                {
                    p_list.put(p, 0);
                }
            }
            if(type.size()!=0)
            {
                for(String t:type)
                {
                    t_list.put(t, 0);
                }
            }
                

            //实现从给定的日志路径中筛选出需要进行统计的文件路径
            File file=new File(log_Path);
            LinkedList<File> filelist=new LinkedList<File>();
            if(file.exists())//路径存在
            {
                //用户未指定日期
                if(date.length()==0)
                {
                    for(File file2:file.listFiles())
                    {
                        filelist.add(file2);
                    }
                }
                //用户指定日期
                else
                {
                    String templog=log_Path+"\\"+date+".log.txt";
                    for(File file2:file.listFiles())
                    {
                        if(file2.getAbsolutePath().compareTo(templog)<=0)
                        {
                            filelist.add(file2);
                        }
                    }
                }
            }
            else
            {
                System.out.println("您输入的日志文件路径有误");
                return;
            }


            //实现从日志文件中获取数据
            for(File file2:filelist)
            {
                try
                {
                    InputStream fr=new FileInputStream(file2.getAbsolutePath());
                    BufferedReader br=new BufferedReader(new InputStreamReader(fr,StandardCharsets.UTF_8));
                    String line="";
                    while((line=br.readLine())!=null&&line.charAt(0)!='/')
                    {
                        String [] ss=line.split(" ");
                        int num=0;
                        String Num="";
                        String numStr=ss[ss.length-1];
                        numStr=numStr.trim();
                        for(int i=0;i<numStr.length();++i)
                        {
                            if(numStr.charAt(i)>=48&&numStr.charAt(i)<=57)
                            {
                                Num+=numStr.charAt(i);
                            }
                        }
                        num=Integer.parseInt(Num);
                        if(line.contains("新增"))
                        {
                            //未指定省份时自己添加
                            if(province.size()==0)
                            {
                                p_list.put(ss[0], 0);
                            }
                            if(line.contains("感染患者"))
                            {
                                ip+=num;
                                //判断是否存在对应省份
                                if(ip_list.containsKey(ss[0]))
                                {
                                    ip_list.put(ss[0], ip_list.get(ss[0])+num);
                                }
                                else
                                {
                                    ip_list.put(ss[0], num);
                                }
                            }
                            else
                            {
                                sp+=num;
                                //判断是否存在对应省份
                                if(sp_list.containsKey(ss[0]))
                                {
                                    sp_list.put(ss[0], sp_list.get(ss[0])+num);
                                }
                                else
                                {
                                    sp_list.put(ss[0], num);
                                }
                            }
                        }
                        else if(line.contains("流入"))
                        {
                            if(province.size()==0)
                            {
                                p_list.put(ss[0], 0);
                                p_list.put(ss[3], 0);
                            }
                            if(line.contains("感染患者"))
                            {
                                //流出省必定已存在，无需判断
                                ip_list.put(ss[0], ip_list.get(ss[0])-num);
                                if(ip_list.containsKey(ss[3]))
                                {
                                    ip_list.put(ss[3], ip_list.get(ss[3])+num);
                                }
                                else
                                {
                                    ip_list.put(ss[3], num);
                                }
                            }
                            else
                            {
                                //流出省必定已存在，无需判断
                                sp_list.put(ss[0], sp_list.get(ss[0])-num);
                                if(sp_list.containsKey(ss[3]))
                                {
                                    sp_list.put(ss[3], sp_list.get(ss[3])+num);
                                }
                                else
                                {
                                    sp_list.put(ss[3], num);
                                }
                            }
                        }
                        else if(line.contains("排除"))
                        {
                            sp-=num;
                           sp_list.put(ss[0], sp_list.get(ss[0])-num);
                        }
                        else if(line.contains("死亡"))
                        {
                            if(province.size()==0)
                            {
                                p_list.put(ss[0], 0);
                            }
                            dead+=num;
                            //死亡时感染患者减少
                            ip-=num;
                            ip_list.put(ss[0], ip_list.get(ss[0])-num);
                            if(dead_list.containsKey(ss[0]))
                            {
                                dead_list.put(ss[0], dead_list.get(ss[0])+num);
                            }
                            else
                            {
                                dead_list.put(ss[0], num);
                            }
                        }
                        else if(line.contains("治愈"))
                        {
                            cure+=num;
                            //治愈时感染患者减少
                            ip-=num;
                            ip_list.put(ss[0], ip_list.get(ss[0])-num);
                            if(cure_list.containsKey(ss[0]))
                            {
                                cure_list.put(ss[0], cure_list.get(ss[0])+num);
                            }
                            else
                            {
                                cure_list.put(ss[0], num);
                            }
                        }
                        else if(line.contains("确诊"))
                        {
                            sp-=num;
                            ip+=num;
                            //因为是确诊，肯定是疑似转为确认，无需判断疑似
                            sp_list.put(ss[0], sp_list.get(ss[0])-num);
                            if(ip_list.containsKey(ss[0]))
                            {
                                ip_list.put(ss[0], ip_list.get(ss[0])+num);
                            }
                            else
                            {
                                ip_list.put(ss[0], num);
                            }
                        }
                    }
                    br.close();
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
                
            }


            //实现将从日志中获取的数据输出至指定位置的功能
            File outFile=new File(out_Path);
            try
            {
                //检查文件是否存在，不存在则创建
                if(!outFile.exists())
                {
                    outFile.createNewFile();
                }

                //覆盖原有内容
                FileOutputStream os=new FileOutputStream(out_Path);
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,StandardCharsets.UTF_8));
                //未指定省份地区时
                if(province.size()==0)
                {
                    writer.write("全国 ");
                    //未指定统计类型
                    if(type.size()==0)
                    {
                        writer.write("感染患者"+ip+"人 ");
                        writer.write("疑似患者"+sp+"人 ");
                        writer.write("治愈"+cure+"人 ");
                        writer.write("死亡"+dead+"人 ");
                    }
                    else
                    {
                        if(t_list.containsKey("ip"))
                        {
                            writer.write("感染患者"+ip+"人 ");
                        }
                        if(t_list.containsKey("sp"))
                        {
                            writer.write("疑似患者"+sp+"人 ");
                        }
                        if(t_list.containsKey("cure"))
                        {
                            writer.write("治愈"+cure+"人 ");
                        }
                        if(t_list.containsKey("dead"))
                        {
                            System.out.print("死亡"+dead+"人 ");
                        }
                    }
                    writer.write("\n");
                }
                Iterator<String> p_iterator=p_list.keySet().iterator();
                while(p_iterator.hasNext())
                {
                    String key=p_iterator.next();
                    if(key.equals("全国"))
                    {
                        //未指定统计类型
                        writer.write("全国 ");
                        if(type.size()==0)
                        {
                            writer.write("感染患者"+ip+"人 ");
                            writer.write("疑似患者"+sp+"人 ");
                            writer.write("治愈"+cure+"人 ");
                            writer.write("死亡"+dead+"人 ");
                        }
                        else
                        {
                            if(t_list.containsKey("ip"))
                            {
                                writer.write("感染患者"+ip+"人 ");
                            }
                            if(t_list.containsKey("sp"))
                            {
                                writer.write("疑似患者"+sp+"人 ");
                            }
                            if(t_list.containsKey("cure"))
                            {
                                writer.write("治愈"+cure+"人 ");
                            }
                            if(t_list.containsKey("dead"))
                            {
                                System.out.print("死亡"+dead+"人 ");
                            }
                        }
                        writer.write("\n");
                    }
                    //其余都为省份名
                    else
                    {
                            
                        if(!cure_list.containsKey(key))
                        {
                            cure_list.put(key, 0);
                        }
                        if(!dead_list.containsKey(key))
                        {
                            dead_list.put(key, 0);
                        }
                        writer.write(key+" ");
                        //未指定统计类型
                        if(type.size()==0)
                        {
                                writer.write("感染患者"+ip_list.get(key)+"人 ");
                                writer.write("疑似患者"+sp_list.get(key)+"人 ");
                                writer.write("治愈"+cure_list.get(key)+"人 ");
                                writer.write("死亡"+dead_list.get(key)+"人 ");
                        }
                        else
                        {
                            if(t_list.containsKey("ip"))
                            {
                                writer.write("感染患者"+ip_list.get(key)+"人 ");
                            }
                            if(t_list.containsKey("sp"))
                            {
                                writer.write("疑似患者"+sp_list.get(key)+"人 ");
                            }
                            if(t_list.containsKey("cure"))
                            {
                                writer.write("治愈"+cure_list.get(key)+"人 ");
                            }
                            if(t_list.containsKey("dead"))
                            {
                                writer.write("死亡"+dead_list.get(key)+"人 ");
                            }
                        }
                        writer.write("\n");
                    }
                }
                writer.flush();
                writer.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
