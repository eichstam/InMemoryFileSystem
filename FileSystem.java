import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 588. Design In-Memory File System

  Design an in-memory file system to simulate the following functions:

  ls: Given a path in string format. If it is a file path, return a list that
  only contains this file's name. If it is a directory path, return the list
  of file and directory names in this directory. Your output (file and
  directory names together) should in lexicographic order.

  mkdir: Given a directory path that does not exist, you should make a new
  directory according to the path. If the middle directories in the path
  don't exist either, you should create them as well. This function has
  void return type.

  addContentToFile: Given a file path and file content in string format.
  If the file doesn't exist, you need to create that file containing given
  content. If the file already exists, you need to append given content
  to original content. This function has void return type.

  readContentFromFile: Given a file path, return its content in string format.
*/

public class FileSystem {

  class Node {
    boolean isDirectory;
    String name;
    String content;
    HashMap<String, Node> nodes;
    
    Node(String name) {
      this.name = name;
      this.nodes = new HashMap<String, Node>();
    }
  }

  Node root;
  public FileSystem() {
    root = new Node("/");
    root.isDirectory =  true;
  }

  public List<String> ls(String path) {
      ArrayList<String> list = new ArrayList<String>();

      Node currDir = root;
      String[] sa = path.split("/");
      for(int i = 0; i < sa.length; i++) {
        if(sa[i].length() > 0) {
          String currSubpath = sa[i];
          // check if currDir contains currSubpath, continue
          if(currDir.nodes.containsKey(currSubpath)) {
            currDir = currDir.nodes.get(currSubpath);
          }
        }
      }
      if(currDir.isDirectory) {
        Set<String> set = currDir.nodes.keySet();
        Iterator it = set.iterator();
        while(it.hasNext()) {
          String name = (String) it.next();
          list.add(name);
        }
        list.sort(null);
      } else {
        list.add(currDir.name);
      }
      return list;
  }

  // mkdir /a/b/c
  public void mkdir(String path) {
    Node currDir = root;
    String[] sa = path.split("/");
    for(int i = 0; i < sa.length; i++) {
      if(sa[i].length() > 0) {
        String currSubpath = sa[i];
        // check if currDir contains currSubpath, continue
        if(currDir.nodes.containsKey(currSubpath)) {
          currDir = currDir.nodes.get(currSubpath);
        } else {
          // else create directory
          Node aNode = new Node(currSubpath);
          aNode.isDirectory = true;
          currDir.nodes.put(currSubpath, aNode);
          currDir = aNode;
        }
      }
    }
  }
  
  public void addContentToFile(String filePath, String content) {
    // create directory path
    int li = filePath.lastIndexOf('/');
    mkdir(filePath.substring(0, li));

    Node currDir = root;
    String[] sa = filePath.split("/");
    for(int i = 0; i < sa.length; i++) {
      if(sa[i].length() > 0) {
        String currSubpath = sa[i];
        // check if currDir contains currSubpath, continue
        if(currDir.nodes.containsKey(currSubpath)) {
          currDir = currDir.nodes.get(currSubpath);
        } else {
          // else create new file
          Node aNode = new Node(currSubpath);
          aNode.isDirectory = false;
          currDir.nodes.put(currSubpath, aNode);
          currDir = aNode;
        }
      }
    }
    // add content to the file
    if(currDir.content == null) {
      currDir.content = content;
    } else {
      currDir.content += content;
    }
  }
  
  public String readContentFromFile(String filePath) {
    Node currDir = root;
    String[] sa = filePath.split("/");
    for(int i = 0; i < sa.length; i++) {
      if(sa[i].length() > 0) {
        String currSubpath = sa[i];
        // check if currDir contains currSubpath, continue
        if(currDir.nodes.containsKey(currSubpath)) {
          currDir = currDir.nodes.get(currSubpath);
        } else {
          // else the file does not exist
        }
      }
    }
    // get content from file
    String content = "";
    if(currDir.isDirectory) {
      System.out.println("ERROR: trying to read content from a directory");
    } else {
      content = currDir.content;
    }
    return content;
  }
  
  public static void main(String[] args) {
    // ["FileSystem","ls","mkdir","addContentToFile","ls","readContentFromFile"]
    // [[],["/"],["/a/b/c"],["/a/b/c/d","hello"],["/"],["/a/b/c/d"]]
    FileSystem fs = new FileSystem();
    List<String> list = fs.ls("/");
    Iterator it = list.iterator();
    while(it.hasNext()) {
      System.out.println(" " + it.next());
    }
    fs.mkdir("/a/b/c");
    fs.addContentToFile("/a/b/c/d", "hello");
    list = fs.ls("/");
    it = list.iterator();
    System.out.println("ls /");
    while(it.hasNext()) {
      System.out.println(" " + it.next());
    }
    list = fs.ls("/a/b");
    it = list.iterator();
    System.out.println("ls /a/b");
    while(it.hasNext()) {
      System.out.println(" " + it.next());
    }
    String content = fs.readContentFromFile("/a/b/c/d");
    System.out.println("content:" + content);
    list = fs.ls("/a/b/c/d");
    it = list.iterator();
    System.out.println("ls /a/b/c/d");
    while(it.hasNext()) {
      System.out.println(" " + it.next());
    }

  }
}

/**
* Your FileSystem object will be instantiated and called as such:
* FileSystem obj = new FileSystem();
* List<String> param_1 = obj.ls(path);
* obj.mkdir(path);
* obj.addContentToFile(filePath,content);
* String param_4 = obj.readContentFromFile(filePath);
*/
