/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * RandomGUID
 * @version 1.2.1 11/05/02
 * @author Marc A. Mnich
 *
 * From www.JavaExchange.com, Open Software licensing
 *
 * 11/05/02 -- Performance enhancement from Mike Dubman.
 *             Moved InetAddr.getLocal to static block.  Mike has measured
 *             a 10 fold improvement in run time.
 * 01/29/02 -- Bug fix: Improper seeding of nonsecure Random object
 *             caused duplicate GUIDs to be produced.  Random object
 *             is now only created once per JVM.
 * 01/19/02 -- Modified random seeding and added new constructor
 *             to allow secure random feature.
 * 01/14/02 -- Added random function seeding with JVM run time
 *
 */

package org.jspresso.framework.util.uid;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In the multitude of java GUID generators, I found none that guaranteed
 * randomness. GUIDs are guaranteed to be globally unique by using ethernet
 * MACs, IP addresses, time elements, and sequential numbers. GUIDs are not
 * expected to be random and most often are easy/possible to guess given a
 * sample from a given generator. SQL Server, for example generates GUID that
 * are unique but sequential within a given instance.
 * <p>
 * GUIDs can be used as security devices to hide things such as files within a
 * filesystem where listings are unavailable (e.g. files that are served up from
 * a Web server with indexing turned off). This may be desirable in cases where
 * standard authentication is not appropriate. In this scenario, the RandomGUIDs
 * are used as directories. Another example is the use of GUIDs for primary keys
 * in a database where you want to ensure that the keys are secret. Random GUIDs
 * can then be used in a URL to prevent hackers (or users) from accessing
 * records by guessing or simply by incrementing sequential numbers.
 * <p>
 * There are many other possibilities of using GUIDs in the realm of security and
 * encryption where the element of randomness is important. This class was
 * written for these purposes but can also be used as a general purpose GUID
 * generator as well.
 * <p>
 * RandomGUID generates truly random GUIDs by using the system's IP address
 * (name/IP), system time in milliseconds (as an integer), and a very large
 * random number joined together in a single String that is passed through an
 * MD5 hash. The IP address and system time make the MD5 seed globally unique
 * and the random number guarantees that the generated GUIDs will have no
 * discernable pattern and cannot be guessed given any number of previously
 * generated GUIDs. It is generally not possible to access the seed information
 * (IP, time, random number) from the resulting GUIDs as the MD5 hash algorithm
 * provides one way encryption.
 * <p>
 * ----> Security of RandomGUID: <----- RandomGUID can be called one of two ways
 * -- with the basic java Random number generator or a cryptographically strong
 * random generator (SecureRandom). The choice is offered because the secure
 * random generator takes about 3.5 times longer to generate its random numbers
 * and this performance hit may not be worth the added security especially
 * considering the basic generator is seeded with a cryptographically strong
 * random seed.
 * <p>
 * Seeding the basic generator in this way effectively decouples the random
 * numbers from the time component making it virtually impossible to predict the
 * random number component even if one had absolute knowledge of the System
 * time. Thanks to Ashutosh Narhari for the suggestion of using the static
 * method to prime the basic random generator.
 * <p>
 * Using the secure random option, this class compiles with the statistical
 * random number generator tests specified in FIPS 140-2, Security Requirements
 * for Cryptographic Modules, section 4.9.1.
 * <p>
 * I converted all the pieces of the seed to a String before handing it over to
 * the MD5 hash so that you could print it out to make sure it contains the data
 * you expect to see and to give a nice warm fuzzy. If you need better
 * performance, you may want to stick to byte[] arrays.
 * <p>
 * I believe that it is important that the algorithm for generating random GUIDs
 * be open for inspection and modification. This class is free for all uses.
 * <p>
 * Marc
 * <p>
 *
 * @version 1.2.1 11/05/02
 * @author Marc A. Mnich
 */
public class RandomGUID {

  private static final Logger LOG = LoggerFactory
      .getLogger(RandomGUID.class);

  private static final Random       MY_RAND;
  private static final SecureRandom MY_SECURE_RAND;
  private static final String       S_ID;

  private final String separator;
  private String valueAfterMD5  = "";
  private String valueBeforeMD5 = "";

  /**
   * Static block to take care of one time secureRandom seed. It takes a few
   * seconds to initialize SecureRandom. You might want to consider removing
   * this static block or replacing it with a "time since first loaded" seed to
   * reduce this time. This block will run only once per JVM instance.
   */
  static {
    MY_SECURE_RAND = new SecureRandom();
    long secureInitializer = MY_SECURE_RAND.nextLong();
    MY_RAND = new Random(secureInitializer);
    String inetAddr = null;
    try {
      inetAddr = InetAddress.getLocalHost().toString();
    } catch (UnknownHostException e) {
      LOG.error("An unexpected error occurred while initializing the GUID generator.", e);
    }
    S_ID = inetAddr;
  }

  /**
   * Default constructor. With no specification of security option, this
   * constructor defaults to lower security, high performance.
   */
  public RandomGUID() {
    this(false);
  }

  /**
   * Constructor with security option.
   *
   * @param secure
   *          Setting secure true enables each random number generated to be
   *          cryptographically strong. Secure false defaults to the standard
   *          Random function seeded with a single cryptographically strong
   *          random number.
   */
  public RandomGUID(boolean secure) {
    this(secure, "-");
  }

  /**
   * Constructor with security option.
   *
   * @param secure
   *          Setting secure true enables each random number generated to be
   *          cryptographically strong. Secure false defaults to the standard
   *          Random function seeded with a single cryptographically strong
   *          random number.
   * @param separator
   *          the separator to use between the different GUID parts.
   *          {@code null} means no separator.
   */
  public RandomGUID(boolean secure, String separator) {
    this.separator = separator;
    getRandomGUID(secure);
  }

  /**
   * Demonstration and self test of class.
   *
   * @param args
   *          program arguments.
   */
  public static void main(String... args) {
    for (int i = 0; i < 10000; i++) {
      RandomGUID myGUID = new RandomGUID();
      LOG.info("Seeding String={}", myGUID.valueBeforeMD5);
      LOG.info("rawGUID={}", myGUID.valueAfterMD5);
      LOG.info("RandomGUID={}", myGUID.toString());
    }
  }

  /**
   * Convert to the standard format for GUID (Useful for SQL Server
   * UniqueIdentifiers, etc.). Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    String raw = valueAfterMD5.toUpperCase();
    StringBuilder sb = new StringBuilder();
    sb.append(raw.substring(0, 8));
    if (separator != null) {
      sb.append(separator);
    }
    sb.append(raw.substring(8, 12));
    if (separator != null) {
      sb.append(separator);
    }
    sb.append(raw.substring(12, 16));
    if (separator != null) {
      sb.append(separator);
    }
    sb.append(raw.substring(16, 20));
    if (separator != null) {
      sb.append(separator);
    }
    sb.append(raw.substring(20));

    return sb.toString();
  }

  /**
   * Method to generate the random GUID.
   */
  private void getRandomGUID(boolean secure) {
    try {
      StringBuilder sbValueBeforeMD5 = new StringBuilder();
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      long time = System.currentTimeMillis();
      long rand;

      if (secure) {
        rand = MY_SECURE_RAND.nextLong();
      } else {
        rand = MY_RAND.nextLong();
      }

      // This StringBuilder can be a long as you need; the MD5
      // hash will always return 128 bits. You can change
      // the seed to include anything you want here.
      // You could even stream a file through the MD5 making
      // the odds of guessing it at least as great as that
      // of guessing the contents of the file!
      sbValueBeforeMD5.append(S_ID);
      sbValueBeforeMD5.append(":");
      sbValueBeforeMD5.append(Long.toString(time));
      sbValueBeforeMD5.append(":");
      sbValueBeforeMD5.append(Long.toString(rand));

      valueBeforeMD5 = sbValueBeforeMD5.toString();
      md5.update(valueBeforeMD5.getBytes());

      byte[] array = md5.digest();
      StringBuilder sb = new StringBuilder();
      for (byte anArray : array) {
        int b = anArray & 0xFF;
        if (b < 0x10) {
          sb.append('0');
        }
        sb.append(Integer.toHexString(b));
      }

      valueAfterMD5 = sb.toString();

    } catch (NoSuchAlgorithmException e) {
      LOG.error("The checksum algorithm is undefined", e);
    }
  }
}
