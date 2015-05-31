class Test {
    public static void main(String[] args) {


        please show the square_root of 144

    }

    static def please(action) {
        [the: { what ->
            [of: { n -> action(what(n))}]
        }]
    }

    static def show = { println it }
    static def square_root = { Math.sqrt(it  )}


}
