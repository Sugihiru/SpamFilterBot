import math
import re

EMAIL_REGEX = re.compile(r'[^@]+@[^@]+\.[^@]+')


class SpamDetector():
    def __init__(self, spam_vector, avg_len):
        self.spam_vector = spam_vector
        print(self.spam_vector)
        self.spam_vector_norm = self.norm(self.spam_vector)
        self.avg_length = avg_len
        print(self.avg_length)

    def is_spam(self, sentence, vector):
        # Check if sentence contains a link or en email
        # Note : we do not need a strict check
        if 'http://' in sentence or 'https://' in sentence:
            return False
        if EMAIL_REGEX.match(sentence):
            return False

        # Length threshold
        if len(vector) - self.avg_length > 8:
            return False

        # Compute a spam score using spam_vector
        spam_cosine_sim = self.compute_cosine_similarity(vector,
                                                         self.spam_vector)
        print("{0}\n\tScore: {1}".format(sentence, spam_cosine_sim))

        # Threshold spam
        if spam_cosine_sim < 0.1:
            return False
        return True

    def compute_cosine_similarity(self, v1, v2):
        cos_theta = self.dot_product(v1, v2)
        norm_mult = (self.norm(v1) * self.norm(v2))
        if norm_mult != 0:
            cos_theta /= norm_mult
        else:
            cos_theta = 0
        return cos_theta

    def dot_product(self, v1, v2):
        result = 0
        for k in v1:
            result += v1[k] * v2[k]
        return result

    def norm(self, vector):
        try:
            if vector == self.spam_vector:
                return self.spam_vector_norm
        except AttributeError:
            pass

        norm = 0
        for k, v in vector.items():
            norm += math.pow(v, 2)
        norm = math.sqrt(norm)
        return norm
