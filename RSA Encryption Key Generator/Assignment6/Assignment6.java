/*
1 Overview

The objective of this project is to implement components of the RSA cryptosystem. You will implement
three modules that make up this cryptosystem.

Key setup: This module will compute and output the keys: public and private. The keys will be output
to two separate files named public key and private key. The public key file will consist of two integers, n
and e, each given in a separate line.

Encryption: This module will take the public key and a message to be encrypted as the inputs. They
will be read from the files public key and message, respectively. The module will output the ciphertext
(encrypted message) which will be stored in a file named ciphertext.

Decryption: This module will take the public key, the private key and the ciphertext to be decrypted as the
inputs. They will be read from the files public key, private key and ciphertext, respectively. The module
will output the decrypted message and store it in a file named decrypted message.

In the project you will have to do computations on very large integers (at least 200-digit numbers in
decimal). Therefore, you will need do use a language with built in large integers (such as Python) or
use existing large integer package (such as PARI/GP). You can also choose to implement your own large
integer arithmetic package.


2 Key setup

To compute the keys you need to implement a modular exponentiation algorithm, that is, an algorithm
that takes integers x, a and n, and returns xa(mod n). Use the algorithm described in class. Modular
exponentiation will also be used in encryption and decryption modules.

You will need to implement an algorithm to generate large prime numbers. Two key components
of that algorithm are: (1) an algorithm to generate large integers at random, and (2) a primality testing
algorithm. You can use Fermat Primality Test or Miller-Rabin Primality Test.

Finally, you will have to implement the Extended Euclid Algorithm, needed to compute multiplicative
inverses.

To set up a public key you will generate two different prime numbers p and q with at least 100 decimal
digits each, and making sure the difference between them is at least 1095.

Next you will compute n = pq and choose an integer e relatively prime to (p −1)(q −1). Usually,
e = 2^16 + 1 = 65537 is a good choice (you will need to check that it works). At this point your public key
(n, e) is ready. To compute the private key d you will apply the Extended Euclid Algorithm to compute
d = e−1 (mod (p −1)(q −1)).

3 Encryption and Decryption

The message to be encrypted will appear as a sequence of digits (an integer with al least 150 decimal digits
but within the bound given by n). You will encrypt it using the public key and the modular exponentiation
algorithm
To decrypt the ciphertext, you will use the private key and apply the modular exponentiation algorithm.

4 Programming languages/systems

To write your program you have to use one of the following programming languages: Python, C/C++,
or Java. Python and Java have large-number arithmetic built in. Examples of C libraries implementing
large-number arithmetic are: PARI/GP and GMP.

You cannot use source code for any components of this project from the web or elsewhere.
If you use C/C++, you should use OpenStack ubuntu virtual machine (the common choice in other CS
classes) to write and compile the source code. If you use Java or Python, use Java 1.8, python 2.7.12 or
python 3.5.2. If you have questions contact the grader directly.

5 Documentation

Submit a single .zip file which should contain the following files:
•A narrative report (in pdf format) describing how your program works and what algorithms you
implemented. List the major components of the program and explain how they fit together. Describe
how you tested the program and how you verified its correctness. Write about the problems that
you encountered and how you overcame them. If your program does not work correctly, explain
what parts do not work and what parts you believe work correctly. The report should also contain
compilation and execution instructions for your program.

•The source code for all modules implemented, thoroughly documented. In each module explain the
functionality of the module (what is the input and the output). Describe in comments all important
variables.
•The results of a test run for the message (a single integer of 180 digits):
777777777777777000000000000000222222222222222333333333333333444444444444444
222222222222222555555555555555666666666666666777777777777777888888888888888
999999999999999000000000000000
as plaintext. The results of the test should be documented by including the files public key,
private key, message, ciphertext and decrypted message as described in the Overview Section.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.*;
import java.util.Random;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors

public class Assignment6 {

    public static BigInteger getRandomBigInt() {
        Random rand = new Random();
        BigInteger maxLimit = new BigInteger("2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
        BigInteger minLimit = new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        //BigInteger bigInteger = new BigInteger("2");
        BigInteger bigInteger = new BigInteger(333, rand);
        while ((bigInteger.compareTo(minLimit) < 0) || (bigInteger.compareTo(maxLimit) > 0)) {
            bigInteger = new BigInteger(333, rand);
        }
        return bigInteger;
    }

    public static BigInteger makeOdd(BigInteger input) {
        BigInteger randomIntMod = new BigInteger("1");
        randomIntMod = input.mod(new BigInteger("2"));
        BigInteger zero = new BigInteger("0");
        if ((randomIntMod.compareTo(zero)) == 0) {
            input = input.subtract(new BigInteger("1"));
        }
        return input;
    }

    public static boolean checkPrime(BigInteger randomBigInteger) {
        // Setting up 2^s * d + 1 = n
        int s = 0;
        BigInteger d = new BigInteger("1");
        BigInteger testingNum = randomBigInteger.subtract(new BigInteger("1"));

        testingNum = randomBigInteger.subtract(new BigInteger("1"));

        while (((testingNum.mod(new BigInteger("2"))).compareTo(new BigInteger("0"))) == 0) {
            s++;
            //System.out.println(testingNum);
            d = testingNum.divide(new BigInteger("2"));
            testingNum = d;
        }

        //System.out.println("s is " + s);
        //System.out.println("d is " + d);
        BigInteger doesItWork = new BigInteger("1");
        doesItWork = d.multiply(new BigInteger("4"));
        BigInteger base = new BigInteger("2");
        BigInteger shouldBeOne = new BigInteger("0");
        shouldBeOne = base.modPow(doesItWork, randomBigInteger);
        //System.out.println(shouldBeOne);
        if (shouldBeOne.compareTo(new BigInteger("1")) == 0) {
            //System.out.println(randomBigInteger + " is probably prime!");
            return true;
        }
        else {
            //System.out.println(randomBigInteger + " is not prime!");
            return false;
        }
    }

    static BigInteger badModPow(BigInteger base, BigInteger exponent, BigInteger modulo) {
        BigInteger answer = new BigInteger("1");
        BigInteger originalExponent = new BigInteger("1");
        originalExponent = exponent;
        while (exponent.compareTo(new BigInteger("0")) > 0) {
            if (exponent.testBit(0)) { // checks if exponent is odd
                answer = (answer.multiply(base)).mod(modulo);
            }
            exponent = exponent.shiftRight(1);
            base = (base.multiply(base)).mod(modulo);
        }
        answer = answer.mod(modulo);
        //System.out.println(base + "^" + originalExponent + "(mod " + modulo + ") = " + answer);
        return answer;
    }

    static BigInteger modInverse(BigInteger a, BigInteger b)
    {
        BigInteger bInitial = new BigInteger("0");
        bInitial = b;
        BigInteger y = new BigInteger("0");
        BigInteger x = new BigInteger("1");

        while (a.compareTo(new BigInteger("1")) > 0) {
            // q is the quotient
            BigInteger q = new BigInteger("0");
            q = a.divide(b);

            BigInteger t = new BigInteger("0");
            t = b;
            // b is remainder
            // continue process
            b = a.mod(b);
            a = t;
            t = y;

            // update x and y
            y = x.subtract(q.multiply(y));
            x = t;
        }

        // Make x positive if its not
        if (x.compareTo(new BigInteger("0")) < 0) {
            x = x.add(bInitial);
        }
        return x;
    }

    static BigInteger encryption(BigInteger message, BigInteger e, BigInteger n) {
        BigInteger cipherText = new BigInteger("0");
        cipherText = badModPow(message, e, n);
        return cipherText;
    }

    static BigInteger decryption(BigInteger cipherText, BigInteger x, BigInteger n) {
        BigInteger message = new BigInteger("0");
        message = badModPow(cipherText, x, n);
        return message;
    }

    public static void main(String[] args) {

        // Get random 100 digit number

        // Finding 2 Prime Numbers p and q
        BigInteger message = new BigInteger("777777777777777000000000000000222222222222222333333333333333444444444444444222222222222222555555555555555666666666666666777777777777777888888888888888999999999999999000000000000000");
        BigInteger primeNumber1 = new BigInteger("4");
        BigInteger primeNumber2 = new BigInteger("4");

        while (checkPrime(primeNumber1) == false) {
            primeNumber1 = makeOdd(getRandomBigInt());
        }
        while ((checkPrime(primeNumber2) == false) || primeNumber2 == primeNumber1) {
            primeNumber2 = makeOdd(getRandomBigInt());
        }
        System.out.println("Prime Number 1 is: " + primeNumber1); // p
        System.out.println("Prime Number 2 is: " + primeNumber2); // q

        // Computing n = pq
        BigInteger n = new BigInteger("0");
        n = primeNumber1.multiply(primeNumber2);
        //System.out.println("n = " + n);

        // Computing (p - 1)*(q - 1)
        BigInteger eCheck = new BigInteger("0");
        eCheck = (primeNumber1.subtract(new BigInteger("1"))).multiply((primeNumber2.subtract(new BigInteger("1"))));
        BigInteger e = new BigInteger("65537");
        System.out.println("(p-1)*(q-1) = " + eCheck);

        // Checking that e and (p - 1)*(q - 1) are relatively prime
        System.out.println("(p-1)*(q-1) (mod e) = " + eCheck.mod(e));
        if (eCheck.mod(e).compareTo(new BigInteger("0")) == 0) { // Not relatively prime!
            System.out.println("e and (p-1)*(q-1) are not relatively prime! please restart with different e or prime numbers!");
            return;
        }
        else {
            System.out.println("e and (p-1)*(q-1) are relatively prime! Continuing...");

            // PUBLIC KEY!
            System.out.println("Public Key Generated: (" + n + ", " + e + ")");
        }

        // Generating the Private Key (Apply Extended Euclid Algorithm to find d = e^(−1) (mod (p −1)(q −1)).
        // e * x = 1 mod ((p - 1)(q - 1))
        // Finding x:
        BigInteger x = new BigInteger("0");
        x = modInverse(e, eCheck);

        //System.out.println("The inverse of e is " + x);

        // PRIVATE KEY!!!
        System.out.println("Private Key generated: (" + n + ", " + x + ")");

        // Testing!!!
        /*
        message = new BigInteger("4");
        e = new BigInteger("5");
        n = new BigInteger("14");
        x = new BigInteger("11");
        */


        // Ciphertext (encryption)
        BigInteger ciphertext = new BigInteger("0");
        ciphertext = encryption(message, e, n);

        // decrypted_message (decryption)
        BigInteger decrypted_message = new BigInteger("0");
        decrypted_message = decryption(ciphertext, x, n);
        if (decrypted_message.compareTo(message) == 0) {
            //System.out.println("Keys work!");
        }
        else {
            //System.out.println("Keys failed :(");
            //System.out.println("what we got: " + decrypted_message);
        }

        // Creating Files for Public/Private Keys
        try {
            File myObj = new File("public_key.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("public_key.txt");
            myWriter.write("Public Key generated: \n");
            myWriter.write("n: " + n + "\n");
            myWriter.write("e: " + e + "\n");
            myWriter.close();
            System.out.println("Successfully wrote to the public_key file.");
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }
        try {
            File myObj = new File("private_key.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("private_key.txt");
            myWriter.write("Private Key generated: \n");
            myWriter.write("n: " + n + "\n");
            myWriter.write("d: " + x);
            myWriter.close();
            System.out.println("Successfully wrote to the private_key file.");
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }

        // Creating Message txt tile
        try {
            File myObj = new File("message.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("message.txt");
            myWriter.write("message: " + message);
            myWriter.close();
            System.out.println("Successfully wrote to the message file.");
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }

        // Creating ciphertext txt tile
        try {
            File myObj = new File("ciphertext.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("ciphertext.txt");
            myWriter.write("ciphertext: " + ciphertext);
            myWriter.close();
            System.out.println("Successfully wrote to the ciphertext file.");
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }

        // Creating decrypted_message txt tile
        try {
            File myObj = new File("decrypted_message.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("decrypted_message.txt");
            myWriter.write("decrypted message: " + decrypted_message);
            myWriter.close();
            System.out.println("Successfully wrote to the decrypted_message file.");
        } catch (IOException bruh) {
            System.out.println("An error occurred.");
            bruh.printStackTrace();
        }
    }
}