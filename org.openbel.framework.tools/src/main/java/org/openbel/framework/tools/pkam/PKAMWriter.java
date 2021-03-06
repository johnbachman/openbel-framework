/**
 * Copyright (C) 2012 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
 */
package org.openbel.framework.tools.pkam;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;

import javax.crypto.CipherOutputStream;

import org.openbel.framework.core.df.encryption.EncryptionServiceException;
import org.openbel.framework.core.df.encryption.KamStoreEncryptionServiceImpl;

/**
 * PKAMWriter provides a {@link BufferedWriter} that is created from
 * a series of other {@link OutputStream}s.  Data will be processed sequentially
 * by the following streams:<ol>
 * <li>{@link BufferedWriter}, <tt>this</tt>, to write initial data</li>
 * <li>{@link GZIPOutputStream} to compress initial written data</li>
 * <li>{@link CipherOutputStream} to encrypt compressed data</li>
 * <li>{@link FileOutputStream} to write encrypted data to a file</li></ol>
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
class PKAMWriter extends BufferedWriter {

    /**
     * Construct a PKAMWriter that writes a PKAM to <tt>fileName</tt>.  This
     * writer does encryption thus it needs the
     * {@link KamStoreEncryptionServiceImpl} and a <tt>password</tt>.
     * 
     * @param fileName {@link String}, the PKAM file name
     * @param password {@link String}, the password to build the encryption
     * cipher from
     * @param encryptionService {@link KamStoreEncryptionServiceImpl}, the encryption
     * service used to encrypt
     * @throws EncryptionServiceException Thrown if an encryption error
     * occurred during encryption
     * @throws IOException Thrown if an IO error occurred establishing the
     * sequential {@link OutputStream}s
     */
    public PKAMWriter(final String fileName, final String password,
            final KamStoreEncryptionServiceImpl encryptionService)
            throws IOException,
            EncryptionServiceException {
        super(new OutputStreamWriter(new GZIPOutputStream(encryptionService
                .newEncryptingOutputStream(new FileOutputStream(fileName),
                        password))));
    }

    /**
     * Construct a PKAMWriter that writes a PKAM to <tt>fileName</tt>  This
     * writer does not encrypt.
     * 
     * @param fileName {@link String}, the PKAM file name
     * @throws IOException Thrown if an IO error occurred establishing the
     * sequential {@link OutputStream}s
     */
    public PKAMWriter(final String fileName) throws IOException {
        super(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(
                fileName))));
    }
}
