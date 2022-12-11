import groovy.transform.Field

@Field inFile = "in"
//inFile = "sample2"
@Field scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent

enum Command {
    noop,
    addx
}

def main () {
    def data = new ArrayList<String>()
    new File("${scriptDir}/${inFile}").eachLine { line -> data.add(line) }

    def X = 1
    def cycle = 1
    def instructionCounter = 0
    def curCommand = null
    boolean ticking = true
    Integer adding = null
    def sigStrength = 0 // Part 1

    String[][] screenOutput = new String[6][40]

    while(ticking) {
        def parts
        if(curCommand == null) {
            def instruction = data.get(instructionCounter++)
            parts = instruction.split(/\s/)
            String command = parts[0]
            curCommand = Command.valueOf(command)
        }
        // CYCLE START
        cycleStart(instructionCounter, cycle, X, curCommand)

        // DURING CYCLE
        sigStrength += cycleDuring(instructionCounter, cycle, X, curCommand)
        updateScreenDisplay(cycle, X, screenOutput)

        if(adding) { // middle of next cycle
            X+=adding
            adding = null
        } else if(curCommand == Command.addx) {
            adding = parts[1] as Integer
        }

        // CYCLE END
        if(curCommand == Command.noop) { curCommand = null }
        if(curCommand == Command.addx && !adding) { curCommand = null }

        cycle++
        if(!adding) {
            if (instructionCounter >= data.size()) {
                ticking = false // EOF
            }
        }
    }
    println "Part 1: ${sigStrength}"
    displayScreen(screenOutput)
}

private String[][] displayScreen(String[][] screenOutput) {
    screenOutput.each { row ->
        row.each { col ->
            print col == null ? " " : col as String
        }
        println ""
    }
}

main()

static void cycleStart(int counter, int cycle, int x, Command curCommand) {
    // Nothing to do here
}

static void updateScreenDisplay(int cycle, int x, String[][] screen) {
    def scanLine = (cycle-1) / 40 as Integer // scanLine starts at 1
    def rowPos = (cycle-1) % 40 as Integer   // rowPos starts at 0
    if(rowPos == x-1 || rowPos == x || rowPos == x+1)
        screen[scanLine][rowPos] = "#"
//    println "($cycle) ($scanLine) ($rowPos)"
}
static int cycleDuring(int counter, int cycle, int x, Command curCommand) {
    if(cycle % 40 == 20 && cycle <= 220) {
        return cycle * x
    } else {
        return 0
    }
}

static Command cycleEnd(Command curCommand, Integer adding) {
    return curCommand
}