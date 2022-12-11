def inFile = "in"
//inFile = "sample"
def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent

class Node {
    String name
    String fullpath
    Node parent
    Map<String, Node> children = new HashMap<>()
    String type = "dir"
    int size = 0

    Node(def n, def fp, def t) {
        name = n
        fullpath = fp
        type = t
    }

    private addChild(String n, String t, int s=0) {
        Node c = new Node(n, fullpath + "/" + n, t)
        c.parent = this
        c.size = s as int
        children.put(c.name, c)
        if(t.equals("file")) { addSize(s) }
        return c
    }

    def isDir() {
        return (type == "dir")
    }

    private addSize(int s) {
        size += s
        if(parent != null) {
            parent.addSize(s)
        }
    }

    def addDir(String n) {
        return addChild(n, "dir", 0 as int)
    }

    def addFile(String n, int s) {
        return addChild(n, "file", s)
    }

    def printTree(String filter = null, int indent=0) {
        if(filter == null || type.equals(filter)) {
            print "_" * indent
            println "$type: $name [$size]"
        }
        children.sort().each { key, child -> child.printTree(filter, indent+2) }
    }

    def part1(int max) {
        int tot = 0
        children.each {key, child ->
            if(child.isDir()) {
                if (child.size <= max) {
                    tot += child.size
                }
                tot += child.part1(max)
            }
        }
        return tot
    }

    // I don't usually like to pass in an object that then gets updated inside the method...  But this eas quicker and less code
    def part2(def tots, int min) {
        children.each {key, child ->
            if(child.isDir() && child.size > min) {
                tots.add(child.size)
            }
            child.part2(tots, min)
        }
    }

    @Override
    public String toString() {
        return "Node {" +
                "  name='" + name + '\'' + "\n" +
                "  fullpath='" + fullpath + '\'' + "\n" +
                "  parent='" + (parent != null?parent.fullpath:"I am Root") + "'\n" +
                "  children=" + children.keySet().sort() + "\n" +
                "  type='" + type + "'\n" +
                "  size=" + size + "\n" +
                '}\n';
    }
}

static boolean isCommand(String thing) { return thing == '$' }
static boolean isCD(def things) { return isCommand(things.first()) && things[1] == "cd" }
static boolean isDIR(def things) { return !isCommand(things.first()) && things.first() == "dir" }
static boolean isFILE(def things) { return !isCommand(things.first() as String) && things.first() != "dir" }

Node root = new Node("/", "", "dir") // In the beginning, there was root
Map<String, Node> map = new HashMap<String, Node>()

Node curNode = null

// Create the node tree from the input
new File("${scriptDir}/${inFile}").eachLine { line ->
    String[] parts = line.split(/\s/)
    if(isCD(parts)) {
        def dir = parts[2]
        if(dir == "..") {
            curNode = curNode.parent
        } else if (dir == "/") {
            curNode = root
        } else {
            curNode = curNode.children.get(dir)
        }
    }
    if(isDIR(parts)) {
        def dir = parts[1]
        curNode.addDir(dir)
    }
    if(isFILE(parts)) {
        String fl = parts[1]
        int sz = parts[0] as int
        curNode.addFile(fl, sz)
    }
    // If we didn't go into any of the above, we're probably looking at an "$ ls", so ignore, 'cos who cares?
}

// part 1
println "Part 1: " + root.part1(100000)

// part 2
def maxSpace = 70000000
def minNeeded = 30000000
def curUsed = root.size
def unusedSpace = maxSpace - curUsed
def needed = minNeeded - unusedSpace

println "curUsed: $curUsed"
println "unused : $unusedSpace"
println "needed : $needed"

List<Integer> tots = new ArrayList<>()

root.part2(tots, needed)
println "Part 2: " + tots.sort().first()
