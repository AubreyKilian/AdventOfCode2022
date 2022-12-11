import groovy.transform.Field

@Field inFile = "in"
//inFile = "sample2"
@Field scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent

class Pos {
    int x
    int y

    Pos(int x, int y) {
        this.x=x
        this.y=y
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false

        Pos pos = (Pos) o

        if (x != pos.x) return false
        if (y != pos.y) return false

        return true
    }

    @Override
    public String toString() { return "[" + x + "," + y + "]"; }
}

def main () {
    def data = new ArrayList<String>()
    new File("${scriptDir}/${inFile}").eachLine { line -> data.add(line) }


// Assume tail and head start at 0,0, and assume we can go negative too, really...  dunno if we can yet...
    def x = 0
    def y = 0

    List<Pos> headPath = new ArrayList<>()
    headPath.add(new Pos(x, y)) // Get that initial one in there
    data.each {
        def (direction, count) = it.split(/\s/)
        for (def i = 0; i < count as int; i++) {
            if (direction == "U") {
                y++
            }
            if (direction == "D") {
                y--
            }
            if (direction == "R") {
                x++
            }
            if (direction == "L") {
                x--
            }
            headPath.add(new Pos(x, y))
        }
    }

    println "Part 1: " + followHeadPath(2, headPath)
    println "Part 2: " +  followHeadPath(10, headPath)
}

static int followHeadPath(int ropeLength, ArrayList<Pos> headPath) {
    def rope = initLongRope(ropeLength) // index 0 is the head
    List<Pos> visited = new ArrayList<>()
    visited.add(new Pos(0, 0))

    headPath.each { headPosition ->
        rope[0] = headPosition
        for (i in 0..<ropeLength - 1) {
            def farFromSecondKnot = isKnotFarFromSecondKnot(rope[i + 1], rope[i])
            if (farFromSecondKnot) {
                rope[i + 1] = moveKnotTowardsSecondKnot(rope[i + 1], rope[i])
                visited.add(rope[ropeLength - 1])
            }
        }
    }

    return visited.unique().size()
}

static Pos moveKnotTowardsSecondKnot(Pos back, Pos front) {
    Pos moveTo
    if(front.x == back.x) { // on the same row
        moveTo = new Pos(back.x, (front.y > back.y ? back.y+1 : back.y-1))
    }
    else if(front.y == back.y) { // on the same col
        moveTo = new Pos((front.x > back.x ? back.x+1 : back.x-1), back.y)
    }
    else { // diagonal
        moveTo = new Pos((front.x > back.x ? back.x+1 : back.x-1), (front.y > back.y ? back.y+1 : back.y-1))
    }
    return moveTo
}

static boolean isKnotFarFromSecondKnot(Pos back, Pos front) {
    if(front.x == back.x) { // on the same row
        if((front.y - back.y).abs() > 1) { return true }
    }
    else if(front.y == back.y) { // on the same row
        if((front.x - back.x).abs() > 1) { return true }
    }
    else {
        // front and back on a diagonal
        int ys = (front.y - back.y).abs()
        int xs = (front.x - back.x).abs()
        int dist = ys + xs // Crude, but works
        if(dist > 2) {
            return true
        }
    }
    return false
}

static Pos[] initLongRope(int len) {
    def tail = new Pos[len]
    for (i in 0..<len) {
        tail[i] = new Pos(0,0)
    }
    return tail
}

assert isKnotFarFromSecondKnot(new Pos(3,4), new Pos(1,4))
assert !isKnotFarFromSecondKnot(new Pos(0,0), new Pos(0,0))
assert !isKnotFarFromSecondKnot(new Pos(0,0), new Pos(0,1))
assert isKnotFarFromSecondKnot(new Pos(0,0), new Pos(0,2))
assert !isKnotFarFromSecondKnot(new Pos(0,0), new Pos(1,1))
assert isKnotFarFromSecondKnot(new Pos(0,0), new Pos(1,2))
assert isKnotFarFromSecondKnot(new Pos(0,0), new Pos(1,3))
assert moveKnotTowardsSecondKnot(new Pos(0, 0), new Pos(0, 2)) == new Pos(0, 1)
assert moveKnotTowardsSecondKnot(new Pos(0, 0), new Pos(2, 0)) == new Pos(1, 0)
assert moveKnotTowardsSecondKnot(new Pos(0, 0), new Pos(1, 2)) == new Pos(1, 1)
assert moveKnotTowardsSecondKnot(new Pos(0, 0), new Pos(1, 2)) == new Pos(1, 1)
assert moveKnotTowardsSecondKnot(new Pos(0, 0), new Pos(-1, -2)) == new Pos(-1, -1)

main()

