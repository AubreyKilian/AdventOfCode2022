def inFile = "in"
//inFile = "sample"
def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent

class Tree {
    int row
    int col
    int height
    // This really should be an enum
    Tree up
    Tree down
    Tree left
    Tree right

    Tree(def x, def y) {
        this.row = x
        this.col = y
    }

    // region Part 1 - visibility
    def isVisible() {
        def visible = isVisibleFromAbove(this, height) ||
                isVisibleFromBelow(this, height) ||
                isVisibleFromLeft(this, height) ||
                isVisibleFromRight(this, height)
        return visible
    }

    private boolean isVisibleFromAbove(Tree tree, int tall) {
        if (tree.up == null) { return true }
        if (tree.up.height < tall) { return isVisibleFromAbove(tree.up, tall) }
        return false
    }

    private boolean isVisibleFromBelow(Tree tree, int tall) {
        if (tree.down == null) { return true }
        if (tree.down.height < tall) { return isVisibleFromBelow(tree.down, tall) }
        return false
    }

    private boolean isVisibleFromLeft(Tree tree, int tall) {
        if (tree.left == null) { return true }
        if (tree.left.height < tall) { return isVisibleFromLeft(tree.left, tall) }
        return false
    }

    private boolean isVisibleFromRight(Tree tree, int tall) {
        if (tree.right == null) { return true }
        if (tree.right.height < tall) { return isVisibleFromRight(tree.right, tall) }
        return false
    }
    // endregion

    // region Part 2 - scenic score
    int scenicScore() {
        int scoreUp = getScenicScoreOfTrees(getTreesUp())
        int scoreDown = getScenicScoreOfTrees(getTreesDown())
        int scoreLeft = getScenicScoreOfTrees(getTreesLeft())
        int scoreRight = getScenicScoreOfTrees(getTreesRight())
        return scoreUp*scoreDown*scoreLeft*scoreRight
    }

    List<Tree> getTreesUp(List<Tree> inTrees = new ArrayList<Tree>()) {
        inTrees.add(this)
        if(up != null) { return up.getTreesUp(inTrees) }
        return inTrees;
    }

    List<Tree> getTreesDown(List<Tree> inTrees = new ArrayList<Tree>()) {
        inTrees.add(this)
        if(down != null) { return down.getTreesDown(inTrees) }
        return inTrees;
    }

    List<Tree> getTreesLeft(List<Tree> inTrees = new ArrayList<Tree>()) {
        inTrees.add(this)
        if(left != null) { return left.getTreesLeft(inTrees) }
        return inTrees;
    }

    List<Tree> getTreesRight(List<Tree> inTrees = new ArrayList<Tree>()) {
        inTrees.add(this)
        if(right != null) { return right.getTreesRight(inTrees) }
        return inTrees;
    }

    // endregion

    static int getScenicScoreOfTrees(List<Tree> trees) {
        def curTree = trees.pop();
        int score = 0
        trees.any { tree ->
            score++
            return !(tree.height < curTree.height) // break out of the loop if we've hit a tree
        }
        return score
    }

    @Override
    public String toString() {
        return "{" +
                "" + row +
                "," + col +
                "=" + height +
                '}';
    }
}

static Tree[][] createForest(int maxX, int maxY) {
    def forest = new Tree[maxX][maxY]

    // one big forest of nothingness
    for (int x = 0; x < maxX; x++) {
        for (int y = 0; y < maxY; y++) {
            forest[x][y] = new Tree(x, y)
        }
    }

    // Set the neighbour relationships
    for (int x = 0; x < maxX; x++) {
        for (int y = 0; y < maxY; y++) {
            if (x > 0)          { forest[x][y].up    = forest[x - 1][y] }
            if (x < maxX - 1)   { forest[x][y].down  = forest[x + 1][y] }
            if (y > 0)          { forest[x][y].left  = forest[x][y - 1] }
            if (y < maxY - 1)   { forest[x][y].right = forest[x][y + 1] }
        }
    }
    return forest;
}

int forestHeight = 0
def data = new ArrayList<String>()
new File("${scriptDir}/${inFile}").eachLine { line ->
    data.add(line)
    forestHeight = line.length()
}

def forestWidth = data.size()
def forest = createForest(forestWidth, forestHeight)

// OK, now let's go fill the forest with trees
int curRow = 0
data.each { line ->
    int curCol = 0
    line.each { String height ->
        forest[curRow][curCol].height = height as int
        curCol++
    }
    curRow++
}

// So many manual tests!
if(inFile == "sample") {
    assert forest[1][1].height == 5
    assert forest[1][1].right.height == 5
    assert forest[1][1].left.height == 2
    assert forest[1][1].up.height == 0
    assert forest[1][1].down.height == 5
    assert forest[0][0].left == null
    assert forest[0][0].isVisible()
    assert forest[1][1].isVisible()
    assert forest[1][2].isVisible()
    assert !forest[1][3].isVisible()
    assert forest[2][1].isVisible()
    assert !forest[2][2].isVisible()
    assert forest[2][3].isVisible()
    assert !forest[3][1].isVisible()
    assert forest[3][2].isVisible()
    assert !forest[3][3].isVisible()
    assert forest[3][2].getScenicScoreOfTrees((forest[3][2].getTreesUp())) == 2
    assert forest[3][2].getScenicScoreOfTrees((forest[3][2].getTreesLeft())) == 2
    assert forest[3][2].getScenicScoreOfTrees((forest[3][2].getTreesDown())) == 1
    assert forest[3][2].getScenicScoreOfTrees((forest[3][2].getTreesRight())) == 2
    assert forest[1][2].getScenicScoreOfTrees((forest[1][2].getTreesUp())) == 1
    assert forest[1][2].getScenicScoreOfTrees((forest[1][2].getTreesLeft())) == 1
    assert forest[1][2].getScenicScoreOfTrees((forest[1][2].getTreesRight())) == 2
    assert forest[1][2].getScenicScoreOfTrees((forest[1][2].getTreesDown())) == 2
    assert forest[0][0].getScenicScoreOfTrees((forest[0][0].getTreesUp())) == 0
    assert forest[3][2].scenicScore() == 8
    assert forest[1][2].scenicScore() == 4
}

// Part 1
int part1 = 0
for (int x = 0; x < forestWidth; x++) {
    for (int y = 0; y < forestHeight; y++) {
        print(forest[x][y].isVisible() ? "🎄" : "⚫️") // Purty!
        part1 += forest[x][y].isVisible() ? 1 : 0
    }
    println ""
}
println "Part 1: $part1"

// Part 2
List<Integer> part2 = new ArrayList<Integer>()
for (int x = 0; x < forestWidth; x++) {
    for (int y = 0; y < forestHeight; y++) {
        part2.add(forest[x][y].scenicScore() as Integer)
    }
}
part2 = part2.sort()
println "Part 2: " + part2.sort().last()
