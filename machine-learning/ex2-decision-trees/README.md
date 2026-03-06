# Decision Tree Classifier

An entropy-based decision tree classifier implemented from scratch in Python, with support for both discrete and continuous features and optional pruning.

---

## Problem Solved

Build a decision tree that learns axis-aligned splits by maximising information gain at each node, and evaluate its generalisation performance on held-out data.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Split criterion | Information gain = entropy(parent) − weighted average entropy(children); threshold search iterates over sorted unique values for continuous features |
| Stopping conditions | Recursion halts when node is pure, minimum samples threshold is met, or max depth is reached |
| Prediction | Traverses tree from root, following split conditions at each internal node until a leaf is reached |
| Pruning | Post-pruning removes subtrees whose removal improves or maintains validation accuracy |

---

## Key Concepts

- **Entropy:** `H(S) = −Σ p_i · log₂(p_i)`
- **Information gain:** `IG(S, A) = H(S) − Σ |Sᵥ|/|S| · H(Sᵥ)` for each split value v
- **Gini impurity (alternative):** `G = 1 − Σ p_i²`

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Libraries:** NumPy, scikit-learn (evaluation metrics only)
- **Key concepts:** Information gain, entropy, recursive tree construction, pruning, classification

---

## Run

```bash
pip install -r requirements.txt
python solution.py
```
