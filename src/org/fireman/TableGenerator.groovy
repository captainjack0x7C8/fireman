package org.fireman

class TableGenerator {
    private static int margin = 2
    private List<List> rows = new ArrayList<>()
    private int col
    private int[] colLen
    private boolean printHeader = false

    TableGenerator(int col, boolean printHeader) {
        this.printHeader = printHeader
        this.col = col
        this.colLen = new int[col]
    }

    TableGenerator appendRow() {
        List row = new ArrayList(col)
        rows.add(row)
        return this
    }

    TableGenerator appendCol(Object value) {
        if (value == null) {
            value = "NULL"
        }
        List row = rows.get(rows.size() - 1)
        row.add(value)
        int len = value.toString().getBytes().length
        if (colLen[row.size() - 1] < len)
            colLen[row.size() - 1] = len
        return this
    }

    String toString() {
        StringBuilder buf = new StringBuilder()

        int sumlen = 0
        for (int len : colLen) {
            sumlen += len
        }
        if (printHeader)
            buf.append("|").append(printChar('=', sumlen + margin * 2 * col + (col - 1))).append("|\n")
        else
            buf.append("|").append(printChar('-', sumlen + margin * 2 * col + (col - 1))).append("|\n")
        for (int ii = 0; ii < rows.size(); ii++) {
            List row = rows.get(ii)
            for (int i = 0; i < col; i++) {
                String o = ""
                if (i < row.size())
                    o = row.get(i).toString()
                buf.append('|').append(printChar(' ', margin)).append(o)
                buf.append(printChar(' ', colLen[i] - o.getBytes().length + margin))
            }
            buf.append("|\n")
            if (printHeader && ii == 0)
                buf.append("|").append(printChar('=', sumlen + margin * 2 * col + (col - 1))).append("|\n")
            else
                buf.append("|").append(printChar('-', sumlen + margin * 2 * col + (col - 1))).append("|\n")
        }
        return buf.toString()
    }

    private String printChar(String c, Integer len) {
        StringBuilder buf = new StringBuilder()
        for (int i = 0; i < len; i++) {
            buf.append(c)
        }
        return buf.toString()
    }

}
