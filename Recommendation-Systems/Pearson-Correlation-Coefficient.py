# If the data is subject to grade-inflation (different users using different scales), use Pearson

from math import sqrt

# Consider the situation where one user always rates things highly (e.g., 4-5) or low (1-2).
# These large biases in sample sets can create problems within recommendation systems.
# Enter the Pearson Correlation Coefficient:
# It is the measure of correlation between two variables (Such as "Eric" and "Tony") and it ranges from [-1, 1]
# where 1 indicates perfect agreement and -1 indicates perfect disagreement.

# Note: this isn't the *true* pearson coefficient, this is an approximation that we can do in a single-pass algorithm.
users = {"Eric": {"Radioactive": 4.75, "On Top of the World": 4.5, "Hello World": 5.0, "Localhost": 4.25, "8080": 4.0},
		 "Greg": {"Radioactive": 4.0, "On Top of the World": 3.0, "Hello World": 5.0, "Localhost": 2.0, "8080": 1.0}
		 }

def pearsonCorrelationCoefficient(rating1, rating2):
	combined = 0
	sumRatings1 = 0
	sumRatings2 = 0
	sumRatingsSquared1 = 0
	sumRatingsSquared2 = 0
	numKeys = 0
	numerator = 0
	denominator = 0
	for key in rating1:
		if key in rating2: 
			numKeys += 1
			sumRatings1 += rating1[key]
			sumRatingsSquared1 += rating1[key]**2
			sumRatingsSquared2 += rating2[key]**2
			sumRatings2 += rating2[key]
			combined += rating1[key] * rating2[key]

	numerator = combined - (sumRatings1 * sumRatings2 / numKeys)
	denominator = sqrt(sumRatingsSquared1 - sumRatings1**2 / numKeys) * sqrt(sumRatingsSquared2 - sumRatings2**2 / numKeys)
	if denominator == 0:
		return 0
	else:
		return numerator/denominator

print pearsonCorrelationCoefficient(users["Eric"], users["Greg"])