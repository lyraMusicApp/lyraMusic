# HMAC Token Validator
import hmac
import hashlib

def verify_signature(secret: bytes, message: bytes, signature: str) -> bool:
    expected = hmac.new(secret, message, hashlib.sha256).hexdigest()
    return hmac.compare_digest(expected, signature)

# Timestamp: 2026-08-17T09:35:19.129Z
