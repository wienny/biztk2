// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SHA256.java

package KISA;


public class SHA256
{

    private SHA256()
    {
        _Count = new int[4];
        _Buffer = new byte[_SHA256_DIGEST_BLOCKLEN];
    }

    public SHA256(byte bpMessage[])
    {
        _Count = new int[4];
        _Buffer = new byte[_SHA256_DIGEST_BLOCKLEN];
        SHA256_Update(bpMessage);
        SHA256_Final();
    }

    public byte[] GetHashCode()
    {
        return _bpDigest;
    }

    private void SHA256_Update(byte bpMessage[])
    {
        int nMessageLen = bpMessage.length;
        int nMessageIndex = 0;
        if((_Count[0] += nMessageLen << 3) < 0)
            _Count[1]++;
        _Count[1] += nMessageLen >> 29;
        for(; nMessageLen >= _SHA256_DIGEST_BLOCKLEN; nMessageLen -= _SHA256_DIGEST_BLOCKLEN)
        {
            for(int i = 0; i < _SHA256_DIGEST_BLOCKLEN; i++)
                _Buffer[i] = bpMessage[nMessageIndex + i];

            SHA256_Transform();
            nMessageIndex += _SHA256_DIGEST_BLOCKLEN;
        }

        for(int i = 0; i < nMessageLen; i++)
            _Buffer[i] = bpMessage[nMessageIndex + i];

    }

    private void SHA256_Final()
    {
        int nCountL = _Count[0];
        int nCountH = _Count[1];
        int nIndex = (nCountL >> 3) % _SHA256_DIGEST_BLOCKLEN;
        _Buffer[nIndex++] = -128;
        if(nIndex > _SHA256_DIGEST_BLOCKLEN - 8)
        {
            for(int i = nIndex; i < _SHA256_DIGEST_BLOCKLEN; i++)
                _Buffer[i] = 0;

            SHA256_Transform();
            for(int i = 0; i < _SHA256_DIGEST_BLOCKLEN - 8; i++)
                _Buffer[i] = 0;

        } else
        {
            for(int i = nIndex; i < _SHA256_DIGEST_BLOCKLEN - 8; i++)
                _Buffer[i] = 0;

        }
        if(_ENDIAN == _LITTLE)
        {
            nCountL = ENDIAN_REVERSE_ULONG(nCountL);
            nCountH = ENDIAN_REVERSE_ULONG(nCountH);
        }
        for(int i = 0; i < 4; i++)
        {
            _Buffer[_SHA256_DIGEST_BLOCKLEN - 1 - i] = (byte)((nCountL & 255 << 8 * i) >>> 8 * i);
            _Buffer[_SHA256_DIGEST_BLOCKLEN - 5 - i] = (byte)((nCountH & 255 << 8 * i) >>> 8 * i);
        }

        SHA256_Transform();
        _bpDigest = new byte[_SHA256_DIGEST_VALUELEN];
        for(int i = 0; i < _SHA256_DIGEST_VALUELEN; i++)
            _bpDigest[i] = (byte)((_ChainVar[i / 4] & 0xff000000 >>> 8 * (i % 4)) >>> 32 - 8 * ((i + 1) % 4));

    }

    private void SHA256_Transform()
    {
        int X[] = new int[64];
        for(int j = 0; j < 16; j++)
        {
            int T1 = 0;
            for(int i = 0; i < 4; i++)
                T1 |= (0xff & _Buffer[i + 4 * j]) << 24 - 8 * i;

            X[j] = GetData(T1);
        }

        for(int j = 16; j < 64; j++)
            X[j] = RHO1(X[j - 2]) + X[j - 7] + RHO0(X[j - 15]) + X[j - 16];

        int a = _ChainVar[0];
        int b = _ChainVar[1];
        int c = _ChainVar[2];
        int d = _ChainVar[3];
        int e = _ChainVar[4];
        int f = _ChainVar[5];
        int g = _ChainVar[6];
        int h = _ChainVar[7];
        for(int j = 0; j < 64; j += 8)
        {
            int T1 = FF0(e, f, g, h, j + 0, X);
            d += T1;
            h = FF1(T1, a, b, c);
            T1 = FF0(d, e, f, g, j + 1, X);
            c += T1;
            g = FF1(T1, h, a, b);
            T1 = FF0(c, d, e, f, j + 2, X);
            b += T1;
            f = FF1(T1, g, h, a);
            T1 = FF0(b, c, d, e, j + 3, X);
            a += T1;
            e = FF1(T1, f, g, h);
            T1 = FF0(a, b, c, d, j + 4, X);
            h += T1;
            d = FF1(T1, e, f, g);
            T1 = FF0(h, a, b, c, j + 5, X);
            g += T1;
            c = FF1(T1, d, e, f);
            T1 = FF0(g, h, a, b, j + 6, X);
            f += T1;
            b = FF1(T1, c, d, e);
            T1 = FF0(f, g, h, a, j + 7, X);
            e += T1;
            a = FF1(T1, b, c, d);
        }

        _ChainVar[0] += a;
        _ChainVar[1] += b;
        _ChainVar[2] += c;
        _ChainVar[3] += d;
        _ChainVar[4] += e;
        _ChainVar[5] += f;
        _ChainVar[6] += g;
        _ChainVar[7] += h;
    }

    private int FF0(int e, int f, int g, int h, int j, int X[])
    {
        return h + Sigma1(e) + Ch(e, f, g) + _K[j] + X[j];
    }

    private int FF1(int T1, int a, int b, int c)
    {
        return T1 + Sigma0(a) + Maj(a, b, c);
    }

    private int Ch(int x, int y, int z)
    {
        return x & y ^ ~x & z;
    }

    private int Maj(int x, int y, int z)
    {
        return x & y ^ x & z ^ y & z;
    }

    private int Sigma0(int x)
    {
        return RR(x, 2) ^ RR(x, 13) ^ RR(x, 22);
    }

    private int Sigma1(int x)
    {
        return RR(x, 6) ^ RR(x, 11) ^ RR(x, 25);
    }

    private int GetData(int nX)
    {
        if(_ENDIAN == _BIG)
            return nX;
        else
            return ENDIAN_REVERSE_ULONG(nX);
    }

    private int ENDIAN_REVERSE_ULONG(int nX)
    {
        return ROTL_ULONG(nX, 8) & 0xff00ff | ROTL_ULONG(nX, 24) & 0xff00ff00;
    }

    private int ROTL_ULONG(int x, int n)
    {
        return x << n | x >>> 32 - n;
    }

    private int ROTR_ULONG(int x, int n)
    {
        return x >>> n | x << 32 - n;
    }

    private int RR(int x, int n)
    {
        return ROTR_ULONG(x, n);
    }

    private int SS(int x, int n)
    {
        return x >>> n;
    }

    private int RHO0(int x)
    {
        return RR(x, 7) ^ RR(x, 18) ^ SS(x, 3);
    }

    private int RHO1(int x)
    {
        return RR(x, 17) ^ RR(x, 19) ^ SS(x, 10);
    }

    private static Boolean _LITTLE = Boolean.valueOf(false);
    private static Boolean _BIG;
    private static Boolean _ENDIAN;
    private static int _SHA256_DIGEST_BLOCKLEN = 64;
    private static int _SHA256_DIGEST_VALUELEN = 32;
    private static int _K[] = {
        0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5, 0xd807aa98, 0x12835b01, 
        0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1, 0xefbe4786, 0xfc19dc6, 0x240ca1cc, 
        0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 
        0x6ca6351, 0x14292967, 0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, 
        0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, 0x19a4c116, 0x1e376c08, 
        0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 
        0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };
    private int _ChainVar[] = {
        0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
    };
    private int _Count[];
    private byte _Buffer[];
    private byte _bpDigest[];

    static 
    {
        _BIG = Boolean.valueOf(true);
        _ENDIAN = _BIG;
    }
}
